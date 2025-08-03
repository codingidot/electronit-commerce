package kr.hhplus.be.server.order.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.service.CouponService;
import kr.hhplus.be.server.order.dto.OrderRequestDto;
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
		Long couponId = request.getCouponId();
		Optional<CouponEntity> coupon = Optional.empty();
		if (couponId != null) {
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
		Optional<UserEntity> userInfo = userService.getUserInfo(request.getUserId());
		userService.deductBalance(userInfo, totalPrice);//잔액차감
		
		//주문생성
		orderService.createOrder(userInfo.get(), buyProduct, buyCnt, totalPrice, couponId);
	}
}
