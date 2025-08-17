package kr.hhplus.be.server.order.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.annotation.DistributedLock;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.service.CouponService;
import kr.hhplus.be.server.order.dto.OrderRequest;
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
	private static int MAX_RETRY = 5;
	
	OrderFacade(ProductService productService,UserService userService, CouponService couponService, OrderService orderService){
		this.productService = productService;
		this.userService = userService;
		this.couponService = couponService;
		this.orderService = orderService;
	}
	
	@Transactional
	@DistributedLock(type="order", keys = {"'product:' + #request.goodsId", "'user:' + #request.userId"})
	public void order(OrderRequest request) throws Exception {
		//상품정보
		Long goodsId = request.getGoodsId();
		Optional<ProductEntity> productEntity = productService.getOrderProductInfo(goodsId);
		ProductEntity buyProduct = productService.deductStock(productEntity,request.getCount());//재고차감

		//쿠폰정보
		Long couponId = request.getCouponId();
		Optional<CouponEntity> coupon = Optional.empty();
		if (couponId != null && couponId != 0) {
		    coupon = couponService.getCoupon(couponId);
		}
		
		//가격정보
		BigDecimal unitPrice = buyProduct.getPrice();
		int buyCnt = request.getCount();
		BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(buyCnt));

		//쿠폰 디스카운트 적용
		if(coupon.isPresent()) {
			CouponEntity couponInfo = coupon.get();
			totalPrice =  couponService.applyDiscount(couponInfo, unitPrice, buyCnt, request.getUserId());
		}
		
		//유저정보
		UserEntity userInfo = userService.getUserInfo(request.getUserId());
		userService.deductBalance(userInfo, totalPrice);//잔액차감
		
		//주문생성
		orderService.createOrder(userInfo, buyProduct, buyCnt, totalPrice, couponId);
	}
}
