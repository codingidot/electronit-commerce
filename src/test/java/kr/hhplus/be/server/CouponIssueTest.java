package kr.hhplus.be.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
import kr.hhplus.be.server.repository.coupon.CouponRepository;
import kr.hhplus.be.server.service.coupon.CouponService;

@ExtendWith(MockitoExtension.class)
public class CouponIssueTest {

		@Mock
	    private CouponRepository couponRepository;

	    @InjectMocks
	    private CouponService couponService;

	    @Test
	    @DisplayName("정상적으로 쿠폰이 발급되는 경우")
	    void issueCoupon_성공() throws Exception {
	    	// given
	        Long couponId = 1L;
	        Long userId = 100L;
	        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponId, userId);

	        CouponDto coupon = new CouponDto(couponId, "할인쿠폰", "PERCENT", new BigDecimal("10"), true, 5);
	        
	        when(couponRepository.getIssueData(couponId)).thenReturn(3); 
	        when(couponRepository.getCoupon(couponId)).thenReturn(Optional.of(coupon));
	        when(couponRepository.getCouponIssueSeq()).thenReturn(999L);

	        // when
	        couponService.issueCoupon(requestDto); // 예외 발생 X

	        // then
	        verify(couponRepository).issueSave(argThat(issue ->
	            issue.getCouponId().equals(couponId) &&
	            issue.getUserId().equals(userId) &&
	            issue.getUseYn().equals("N")
	        ));
	    }

	    @Test
	    @DisplayName("쿠폰이 존재하지 않을 때 예외 발생")
	    void issueCoupon_쿠폰없음_예외() {
	        // given
	        Long couponId = 1L;
	        Long userId = 100L;
	        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponId, userId);

	        when(couponRepository.getCoupon(couponId)).thenReturn(Optional.empty());

	        // when & then
	        Exception ex = assertThrows(Exception.class, () -> couponService.issueCoupon(requestDto));
	        assertEquals("해당 쿠폰은 존재하지 않습니다.", ex.getMessage());
	    }

	    @Test
	    @DisplayName("쿠폰 수량이 모두 소진된 경우 예외 발생")
	    void issueCoupon_수량초과_예외() {
	        // given
	        Long couponId = 1L;
	        Long userId = 100L;
	        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponId, userId);

	        CouponDto coupon = new CouponDto(couponId, "할인쿠폰", "PERCENT", new BigDecimal("10"), true,3);

	        when(couponRepository.getCoupon(couponId)).thenReturn(Optional.of(coupon));
	        when(couponRepository.getIssueData(couponId)).thenReturn(3); // 이미 3명 발급됨 (마감)

	        // when & then
	        Exception ex = assertThrows(Exception.class, () -> couponService.issueCoupon(requestDto));
	        assertEquals("해당 쿠폰은 선착순 마감되었습니다.", ex.getMessage());
	    }
}
