package kr.hhplus.be.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
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

import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.dto.order.OrderDto;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.dto.user.UserDto;
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

	    // 성공 케이스
	    @Test
	    void orderSuccessTest() throws Exception {
	        // given
	        Long userId = 1L, goodsId = 100L, couponId = 10L;
	        int count = 2;

	        OrderRequestDto request = new OrderRequestDto(userId, goodsId, count, couponId);
	        ProductDto product = new ProductDto(goodsId, "상품", new BigDecimal("1000"), 10, "N",null);
	        UserDto user = new UserDto(userId, "홍길동", new BigDecimal("5000"));
	        CouponDto coupon = new CouponDto(couponId, "민생쿠폰", "PERCENT", new BigDecimal("10"),true);
	        when(productService.getProductList(any())).thenReturn(Arrays.asList(product));
	        when(userService.getBalanceInfo(userId)).thenReturn(user);
	        when(couponService.getCoupon(couponId)).thenReturn(Optional.of(coupon));
	        when(orderService.getOrderSeq()).thenReturn(999L);

	        // when
	        orderFacade.order(request);

	        // then
	        verify(orderService).insertOrder(any(OrderDto.class));
	        verify(userRepository).save(any(UserDto.class));
	    }

	    // 1. 상품 없음 예외
	    @Test
	    void orderWrongProduct() throws Exception {
	        Long userId = 1L, goodsId = 100L;
	        OrderRequestDto request = new OrderRequestDto(userId, goodsId, 10, 11L);
	        UserDto user = new UserDto(userId, "홍길동", new BigDecimal("5000"));
	        
	        when(productService.getProductList(any())).thenReturn(Collections.emptyList());
	        when(userService.getBalanceInfo(userId)).thenReturn(user);

	        Exception exception = assertThrows(Exception.class, () -> {
	            orderFacade.order(request);
	        });

	        assertEquals("해당 상품은 존재하지 않습니다.", exception.getMessage());
	    }

	    // 2. 재고 부족 예외
	    @Test
	    void orderOutOfStock() throws Exception {
	    	Long userId = 1L, goodsId = 100L;
	        OrderRequestDto request = new OrderRequestDto(userId, goodsId, 10, 5L); // 수량 10 요청
	        ProductDto product = new ProductDto(goodsId, "상품", new BigDecimal(3000), 5, "N", null); // 재고 5

	        // 상품 Mock
	        when(productService.getProductList(any())).thenReturn(Arrays.asList(product));

	        // 유저 Mock
	        UserDto user = new UserDto(userId, "홍길동", new BigDecimal("100000")); // 충분한 잔액
	        when(userService.getBalanceInfo(userId)).thenReturn(user);

	        Exception exception = assertThrows(Exception.class, () -> {
	            orderFacade.order(request); // 여기서 재고 부족 예외 발생해야 함
	        });

	        assertEquals("재고가 부족합니다.", exception.getMessage());
	    }

	    // 3. 쿠폰 할인 금액 > 총액 예외
	    @Test
	    void orderCheaperThanCoupon() throws Exception {
	        Long userId = 1L, goodsId = 100L, couponId = 10L;
	        OrderRequestDto request = new OrderRequestDto(userId, goodsId,1, couponId);
	        ProductDto product = new ProductDto(goodsId, "상품", new BigDecimal("1000"), 10, "N", null);
	        UserDto user = new UserDto(userId, "홍길동", new BigDecimal("5000"));
	        CouponDto coupon = new CouponDto(couponId,"할인쿠폰", "DEDUCT", new BigDecimal("2000"), true); // 할인 > 가격

	        when(productService.getProductList(any())).thenReturn(Arrays.asList(product));
	        when(userService.getBalanceInfo(userId)).thenReturn(user);
	        when(couponService.getCoupon(couponId)).thenReturn(Optional.of(coupon));

	        Exception exception = assertThrows(Exception.class, () -> {
	            orderFacade.order(request);
	        });

	        assertEquals("쿠폰 할인 금액이 주문 금액보다 크면 안됩니다.", exception.getMessage());
	    }

	    // 4. 잔액 부족 예외
	    @Test
	    void orderNoMoney() throws Exception {
	        Long userId = 1L, goodsId = 100L;
	        OrderRequestDto request = new OrderRequestDto(userId, goodsId, 1, null);
	        ProductDto product = new ProductDto(goodsId, "상품", new BigDecimal("10000"), 9, "O", 33L);
	        UserDto user = new UserDto(userId, "홍길동", new BigDecimal("5000")); // 잔액 부족

	        when(productService.getProductList(any())).thenReturn(Arrays.asList(product));
	        when(userService.getBalanceInfo(userId)).thenReturn(user);
	        when(couponService.getCoupon(null)).thenReturn(Optional.empty());

	        Exception exception = assertThrows(Exception.class, () -> {
	            orderFacade.order(request);
	        });

	        assertEquals("잔액이 부족합니다.", exception.getMessage());
	    }

}
