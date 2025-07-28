package kr.hhplus.be.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.repository.user.UserRepository;
import kr.hhplus.be.server.service.coupon.CouponService;
import kr.hhplus.be.server.service.order.OrderFacade;
import kr.hhplus.be.server.service.order.OrderService;
import kr.hhplus.be.server.service.product.ProductService;
import kr.hhplus.be.server.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class OrderTest {

	 	@Mock private ProductService productService;
	    @Mock private UserService userService;
	    @Mock private CouponService couponService;
	    @Mock private OrderService orderService;
	    @Mock private UserRepository userRepository;

	    @InjectMocks private OrderFacade orderFacade;


	    @Test
	    void order_with_coupon_success() throws Exception {
	        // given
	        Long userId = 1L;
	        Long goodsId = 100L;
	        Long couponId = 10L;
	        int count = 2;

	        OrderRequestDto request = new OrderRequestDto(userId, goodsId, count, couponId);

	        Product product = new Product(goodsId, "상품", new BigDecimal("1000"), 10, "N", null);
	        User user = new User(userId, "홍길동", new BigDecimal("10000"));
	        Coupon coupon = new Coupon(couponId, "10%쿠폰", "PERCENT", new BigDecimal("10"), true, 10);

	        // Mock return values
	        when(productService.getOrderProductInfo(goodsId)).thenReturn(product);
	        when(userService.getUserInfo(userId)).thenReturn(user);
	        when(couponService.getCoupon(couponId)).thenReturn(Optional.of(coupon));
	        when(couponService.applyDiscount(eq(coupon), eq(product.getPrice()), eq(count)))
	                .thenReturn(new BigDecimal("1800"));  // 10% 할인 적용된 금액

	        // when
	        orderFacade.order(request);

	        // then
	        verify(productService).getOrderProductInfo(goodsId);
	        verify(userService).getUserInfo(userId);
	        verify(couponService).getCoupon(couponId);
	        verify(couponService).applyDiscount(coupon, product.getPrice(), count);
	        verify(orderService).createOrder(user, product, count, new BigDecimal("1800"), couponId);
	        verify(userService).updateUser(argThat(updatedUser ->
	                updatedUser.getBalance().compareTo(new BigDecimal("8200")) == 0  // 10000 - 1800
	        ));
	    }
}
