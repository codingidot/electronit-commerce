package kr.hhplus.be.server.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kr.hhplus.be.server.dto.balance.BalanceDto;
import kr.hhplus.be.server.dto.balance.ChargeRequestDto;
import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
import kr.hhplus.be.server.dto.produt.ProductDto;

@Component
public class FakeData {

    List<ProductDto> productList = new ArrayList<>();

    public List<ProductDto> getProductList(){
        productList.add(new ProductDto(1L, "상품A", new BigDecimal("19900"), 50, "일반", 100L));
        productList.add(new ProductDto(2L, "상품B", new BigDecimal("29900"), 30, "프리미엄", 100L));
        productList.add(new ProductDto(3L, "상품C", new BigDecimal("15900"), 100, "일반", 101L));
        productList.add(new ProductDto(4L, "상품D", new BigDecimal("45900"), 10, "한정판", 102L));

        return productList;
    }
    
    public BalanceDto getBalanceInfo(Long userId) {
    	return new BalanceDto(userId, "김철수", new BigDecimal("10000"));
    }
    
    public BalanceDto chargeBalance(ChargeRequestDto dto) {
    	return new BalanceDto(dto.getUserId(), "김철수", dto.getAmount().add(new BigDecimal("10000")));
    }
    
    public CouponDto issueCoupon(CouponIssueRequestDto dto) {
    	return new CouponDto(dto.getCouponId(), "가입쿠폰", "PERCENT", true);
    }

}
