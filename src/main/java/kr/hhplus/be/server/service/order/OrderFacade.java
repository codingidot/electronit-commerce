package kr.hhplus.be.server.service.order;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
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
		Product buyProduct = productService.getOrderProductInfo(goodsId);
	
		//유저정보
		User userInfo = userService.getUserInfo(request.getUserId());
		
		//쿠폰정보
		Long couponId = request.getCouponId();
		Optional<Coupon> coupon = couponService.getCoupon(couponId);
		
		//가격정보
		BigDecimal unitPrice = buyProduct.getPrice();
		int buyCnt = request.getCount();
		BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(buyCnt));

		//쿠폰 디스카운트 적용
		if(coupon.isPresent()) {
			Coupon couponInfo = coupon.get();
			totalPrice =  couponService.applyDiscount(couponInfo, unitPrice, buyCnt);
		}

		orderService.createOrder(userInfo, buyProduct, buyCnt, totalPrice, couponId);
		
		//유저 잔액 차감
		userService.updateUser(new User(userInfo.getUserId(), userInfo.getUserName(), userInfo.getBalance().subtract(totalPrice)));
		
	}
}
