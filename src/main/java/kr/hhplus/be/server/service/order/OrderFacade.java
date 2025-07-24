package kr.hhplus.be.server.service.order;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.dto.order.OrderDto;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.dto.produt.ProductRequestDto;
import kr.hhplus.be.server.dto.user.UserDto;
import kr.hhplus.be.server.repository.user.UserRepository;
import kr.hhplus.be.server.service.coupon.CouponService;
import kr.hhplus.be.server.service.product.ProductService;
import kr.hhplus.be.server.service.user.UserService;

@Component
public class OrderFacade {
	
	private final ProductService productService;
	private final UserService userService;
	private final CouponService couponService;
	private final OrderService orderService;
	private final UserRepository userRepository;
	
	OrderFacade(ProductService productService,UserService userService, CouponService couponService, OrderService orderService,UserRepository userRepository){
		this.productService = productService;
		this.userService = userService;
		this.couponService = couponService;
		this.orderService = orderService;
		this.userRepository = userRepository;
	}
	
	public void order(OrderRequestDto request) throws Exception {
		//상품정보
		Long goodsId = request.getGoodsId();
		ProductRequestDto param = new ProductRequestDto(goodsId, null, null);
		List<ProductDto> list = productService.getProductList(param);
		
		//유저정보
		UserDto userInfo = userService.getBalanceInfo(request.getUserId());
		BigDecimal balance = userInfo.getBalance();
		
		//쿠폰정보
		Long couponId = request.getCouponId();
		Optional<CouponDto> coupon = couponService.getCoupon(couponId);
		
		//유저체크
		if(list.size() == 0) {
			throw new Exception("해당 상품은 존재하지 않습니다.");
		}

		//재고체크
		int stock = list.get(0).getStock();
		int buyCnt = request.getCount();
		if(buyCnt > stock) {
			throw new Exception("재고가 부족합니다.");
		}
		
		//가격정보
		BigDecimal unitPrice = list.get(0).getPrice();
		BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(buyCnt));
		
		//쿠폰체크
		if(coupon.isPresent()) {
			CouponDto couponInfo = coupon.get();
			String couponType = couponInfo.getCouponType();// PERCENT : 전체에서 퍼센트 할인, DEDUCT : 금액 차감
			BigDecimal couponVal = couponInfo.getCouponValue();
			//쿠폰금액 차감
			if("PERCENT".equals(couponType)) {//퍼센트 할인
				BigDecimal discountRate = couponVal.divide(BigDecimal.valueOf(100));
			    BigDecimal discountAmount = totalPrice.multiply(discountRate);
			    totalPrice = totalPrice.subtract(discountAmount);
			}else if("DEDUCT".equals(couponType)) {//일정 금액 할인
				totalPrice = totalPrice.subtract(couponVal);
				if(totalPrice.compareTo(BigDecimal.ZERO) < 0) {
					throw new Exception("쿠폰 할인 금액이 주문 금액보다 크면 안됩니다.");
				}
			}
		}
		
		//잔액체크
		if(balance.compareTo(totalPrice)<0) {
			throw new Exception("잔액이 부족합니다.");
		}

		//주문 테이블에 insert
		Long orderNewId = orderService.getOrderSeq();
		orderService.insertOrder(new OrderDto(orderNewId, goodsId,couponId, request.getUserId(),unitPrice.multiply(BigDecimal.valueOf(buyCnt))
											  , totalPrice , buyCnt, "10"));
		
		//유저 잔액 차감
		userRepository.save(new UserDto(userInfo.getUserId(), userInfo.getUserName(), userInfo.getBalance().subtract(totalPrice)));
		
	}
}
