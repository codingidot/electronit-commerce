package kr.hhplus.be.server.service.order;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.entity.coupon.CouponEntity;
import kr.hhplus.be.server.entity.product.ProductEntity;
import kr.hhplus.be.server.entity.user.UserEntity;
import kr.hhplus.be.server.service.coupon.CouponService;
import kr.hhplus.be.server.service.product.ProductService;
import kr.hhplus.be.server.service.user.UserService;

@Component
public class OrderFacade {
	
	private final ProductService productService;
	private final UserService userService;
	private final CouponService couponService;
	private final OrderService orderService;
	
	OrderFacade(ProductService productService,UserService userService, CouponService couponService, OrderService orderService){
		this.productService = productService;
		this.userService = userService;
		this.couponService = couponService;
		this.orderService = orderService;
	}
	
	@Transactional(rollbackFor = Exception.class)
	public void order(OrderRequestDto request) throws Exception {
		//상품정보
		Long goodsId = request.getGoodsId();
		Optional<ProductEntity> productEntity = productService.getOrderProductInfo(goodsId);
		productService.deductStock(productEntity,request.getCount());//재고차감
		ProductEntity buyProduct = productEntity.get();
	
		//쿠폰정보
		Long couponId = (Long) request.getCouponId();
		Optional<CouponEntity> coupon = couponService.getCoupon(couponId);
		
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
		Optional<UserEntity> userInfo = userService.getUserInfo(request.getUserId());
		userService.deductBalance(userInfo, totalPrice);//잔액차감
		
		//주문생성
		orderService.createOrder(userInfo.get(), buyProduct, buyCnt, totalPrice, couponId);
	}
}
