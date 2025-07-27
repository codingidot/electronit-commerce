package kr.hhplus.be.server.mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import kr.hhplus.be.server.dto.coupon.Coupon;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
import kr.hhplus.be.server.dto.product.Product;
import kr.hhplus.be.server.dto.user.User;
import kr.hhplus.be.server.dto.user.ChargeRequestDto;

@Component
public class FakeData {

    List<Product> productList = new ArrayList<>();

    public List<Product> getProductList(){
        productList.add(new Product(1L, "상품A", new BigDecimal("19900"), 50, "일반", 100L));
        productList.add(new Product(2L, "상품B", new BigDecimal("29900"), 30, "프리미엄", 100L));
        productList.add(new Product(3L, "상품C", new BigDecimal("15900"), 100, "일반", 101L));
        productList.add(new Product(4L, "상품D", new BigDecimal("45900"), 10, "한정판", 102L));

        return productList;
    }
    
    public User getBalanceInfo(Long userId) {
    	return new User(userId, "김철수", new BigDecimal("10000"));
    }
    
    public User chargeBalance(ChargeRequestDto dto) {
    	return new User(dto.getUserId(), "김철수", dto.getAmount().add(new BigDecimal("10000")));
    }
    
    public Coupon issueCoupon(CouponIssueRequestDto dto) {
    	return new Coupon(dto.getCouponId(), "가입쿠폰", "PERCENT", new BigDecimal(30), true, 10);
    }
}
