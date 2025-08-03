package kr.hhplus.be.server;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.entity.CouponIssueEntity;
import kr.hhplus.be.server.coupon.repository.CouponIssueJpaRepository;
import kr.hhplus.be.server.coupon.repository.CouponJpaRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CouponIntegrationTest {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");
    
    static {
    	mysqlContainer.start(); // ✅ 컨테이너를 먼저 시작
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
        
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CouponJpaRepository couponJpaRepository;

    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;
    
    Long savedCouponId; 

    @BeforeEach
    void setUp() {
        couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();

        CouponEntity coupon = new CouponEntity();
        coupon.setCouponType("DEDUCT");
        coupon.setCouponValue(new BigDecimal("3000"));
        coupon.setCount(100);
        CouponEntity saved = couponJpaRepository.save(coupon);
        savedCouponId = saved.getCouponId();
    }

    @Test
    void 쿠폰_정상_발급() throws Exception {
        Long userId = 10L;

        String body = """
        {
          "userId": %d,
          "couponId": %d
        }
        """.formatted(userId, savedCouponId);

        mockMvc.perform(post("/coupons/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        CouponIssueEntity issued = couponIssueJpaRepository.findByCouponIdAndUserId(savedCouponId, userId);
        assertThat(issued.getCouponId()).isEqualTo(savedCouponId);
    }

    @Test
    void 중복_발급_차단() throws Exception {
        Long userId = 20L;

        CouponIssueEntity entity = new CouponIssueEntity(); 
        entity.setCouponId(savedCouponId);
        entity.setUserId(userId);
        entity.setUseYn("N");

        couponIssueJpaRepository.save(entity);

        String body = """
        {
          "userId": %d,
          "couponId": %d
        }
        """.formatted(userId, savedCouponId);

        mockMvc.perform(post("/coupons/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("쿠폰발급에 실패하였습니다."));
    }

    @Test
    void 없는_쿠폰_발급_시도() throws Exception {
        Long userId = 999L;
        Long couponId = 999L;

        String body = """
        {
          "userId": %d,
          "couponId": %d
        }
        """.formatted(userId, couponId);

        mockMvc.perform(post("/coupons/issue")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("쿠폰발급에 실패하였습니다."));
    }
}