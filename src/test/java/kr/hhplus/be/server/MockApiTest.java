package kr.hhplus.be.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.hhplus.be.server.dto.balance.BalanceDto;
import kr.hhplus.be.server.dto.coupon.CouponDto;
import kr.hhplus.be.server.dto.coupon.CouponIssueRequestDto;
import kr.hhplus.be.server.dto.order.OrderRequestDto;
import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.mock.FakeData;

@WebMvcTest
public class MockApiTest {

	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private FakeData fakeData;

    @Test
    @DisplayName("상품조회 mock api 테스트")
    void getProductInfoTest() throws Exception {
        // given
        List<ProductDto> productList = Arrays.asList(
                new ProductDto(1L, "상품1", new BigDecimal("1000.00"), 10, "TYPE_A", null),
                new ProductDto(2L, "상품2", new BigDecimal("2000.00"), 5, "TYPE_B", 1L)
        );

        Mockito.when(fakeData.getProductList()).thenReturn(productList);

        // when & then
        mockMvc.perform(get("/products")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].goodsId").value(1))
                .andExpect(jsonPath("$.data[0].goodsName").value("상품1"))
                .andExpect(jsonPath("$.data[1].goodsName").value("상품2"));
    }
    
    @Test
    @DisplayName("잔액조회 mock api 테스트")
    void getBalanceInfoTest() throws Exception {
    	// given
        Long userId = 1L;
        BalanceDto bal = new BalanceDto(userId,  "홍길동", new BigDecimal("5000.00"));

        Mockito.when(fakeData.getBalanceInfo(userId)).thenReturn(bal);

        // when & then
        mockMvc.perform(get("/wallet/balance/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.userId").value(userId))
                .andExpect(jsonPath("$.data.userName").value("홍길동"))
                .andExpect(jsonPath("$.data.balance").value(5000.00));
    }

    @Test
    @DisplayName("쿠폰발급 mock api 테스트")
    void issueCouponTest() throws Exception {
        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(1L, 100L);
        String json = new ObjectMapper().writeValueAsString(requestDto);

        CouponDto couponDto = new CouponDto(100L, "가입쿠폰", "PERCENT", true);
        Mockito.when(fakeData.issueCoupon(Mockito.any(CouponIssueRequestDto.class))).thenReturn(couponDto);

        // when & then
        mockMvc.perform(post("/coupons/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.couponId").value(100))
            .andExpect(jsonPath("$.data.couponName").value("가입쿠폰"))
            .andExpect(jsonPath("$.data.couponType").value("PERCENT"))
            .andExpect(jsonPath("$.data.issueSuccess").value(true))
            .andDo(print());
    }
    
    @Test
    @DisplayName("주문 및 결제 mock api 테스트")
    public void orderProductTest() throws Exception {
        OrderRequestDto requestDto = new OrderRequestDto(/*필요한 값들*/);
        String json = new ObjectMapper().writeValueAsString(requestDto);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            // 원하는 응답 값 검증
            .andDo(print());
    }

    	
    
}
