package kr.hhplus.be.server.order.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import kr.hhplus.be.server.annotation.DistributedLock;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.service.CouponService;
import kr.hhplus.be.server.order.dto.OrderRequest;
import kr.hhplus.be.server.order.entity.OrderEntity;
import kr.hhplus.be.server.order.enums.OrderStep;
import kr.hhplus.be.server.order.event.OrderCreatedEvent;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.product.service.ProductService;
import kr.hhplus.be.server.user.entity.UserEntity;
import kr.hhplus.be.server.user.service.UserService;

@Component
public class OrderFacade {
	
	private final ProductService productService;
	private final UserService userService;
	private final CouponService couponService;
	private final OrderService orderService;
	private final StringRedisTemplate redisTemplate;
	private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;
	
	OrderFacade(ProductService productService,UserService userService, CouponService couponService, OrderService orderService, StringRedisTemplate redisTemplate, KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate){
		this.productService = productService;
		this.userService = userService;
		this.couponService = couponService;
		this.orderService = orderService;
		this.redisTemplate = redisTemplate;
		this.kafkaTemplate = kafkaTemplate;
	}
	
	@DistributedLock(type="order", keys = {"'product:' + #request.goodsId", "'user:' + #request.userId"})
	public void order(OrderRequest request) throws Exception {
		
		OrderStep step = OrderStep.CREATED;

	    Long goodsId = request.getGoodsId();
	    Long couponId = request.getCouponId();
	    Long userId = request.getUserId();
	    int buyCnt = request.getCount();
	    BigDecimal totalPrice = BigDecimal.ZERO;
	    ProductEntity buyProduct = null;
	    UserEntity userInfo = null;
	    CouponEntity couponInfo = null;
		
		try {
			//상품정보
			Optional<ProductEntity> productEntity = productService.getOrderProductInfo(goodsId);
			buyProduct = productService.deductStock(productEntity,buyCnt);//재고차감
			step = OrderStep.INVENTORY_DONE;

			//쿠폰정보
			Optional<CouponEntity> coupon = Optional.empty();
			if (couponId != null && couponId != 0) {
			    coupon = couponService.getCoupon(couponId);
			}
			
			//가격정보
			BigDecimal unitPrice = buyProduct.getPrice();
			totalPrice = unitPrice.multiply(BigDecimal.valueOf(buyCnt));

			//쿠폰 디스카운트 적용
			if(coupon.isPresent()) {
				couponInfo = coupon.get();
				totalPrice =  couponService.applyDiscount(couponInfo, unitPrice, buyCnt, userId);
			}
			
			//유저정보
			userInfo = userService.getUserInfo(userId);
			userService.deductBalance(userInfo, totalPrice);//잔액차감
			step = OrderStep.PAYMENT_DONE;
			
			//주문생성
			OrderEntity createdOrder = orderService.createOrder(userInfo, buyProduct, buyCnt, totalPrice, couponId);
			step = OrderStep.COMPLETED;
			
			//주문정보 전송(외부api)
			kafkaTemplate.send("order-event", createdOrder.getOrderId().toString(), new OrderCreatedEvent(createdOrder.getOrderId(), createdOrder.getUserId(), createdOrder.getPayPrice()));
			
			
			//캐시 작업
			//주문 성공 시 상품 주문수 + 1
        	String today = LocalDate.now().toString();
            String key = "product:rank:" + today;
            redisTemplate.opsForZSet().incrementScore(key, goodsId.toString(), 1);
            
            // TTL 확인
            Long expire = redisTemplate.getExpire(key);
            if (expire == null || expire == -1 || expire == -2) { // -1: 만료 없음, -2: 키 없음
                // 오늘 기준 +2일 23:59:59
                LocalDateTime expireAt = LocalDate.now().plusDays(2).atTime(23, 59, 59);
                redisTemplate.expireAt(key, Date.from(expireAt.atZone(ZoneId.systemDefault()).toInstant()));
            }
			
		}catch(Exception e) {
			
	        switch (step) {
	            case INVENTORY_DONE:
	                // 재고차감 취소
	                productService.restoreStock(buyProduct, buyCnt);
	                break;
	            case PAYMENT_DONE:
	                // 유저 잔액 복구
	                userService.refundBalance(userInfo, totalPrice);
	                // 재고차감 취소
	                productService.restoreStock(buyProduct, buyCnt);
	                break;
	            default:
	                break;
	        }
	        throw e;
		}
	}
}
