package kr.hhplus.be.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import java.math.BigDecimal;
import java.util.List;

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

import kr.hhplus.be.server.entity.order.OrderEntity;
import kr.hhplus.be.server.entity.user.UserEntity;
import kr.hhplus.be.server.entity.product.ProductEntity;
import kr.hhplus.be.server.entity.coupon.CouponEntity;
import kr.hhplus.be.server.entity.coupon.CouponIssueEntity;

import kr.hhplus.be.server.repository.order.OrderJpaRepository;
import kr.hhplus.be.server.repository.user.UserJpaRepository;
import kr.hhplus.be.server.repository.product.ProductJpaRepository;
import kr.hhplus.be.server.repository.coupon.CouponJpaRepository;
import kr.hhplus.be.server.repository.coupon.CouponIssueJpaRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderIntegrationTest {

    @Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    static {
        mysqlContainer.start();
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", mysqlContainer::getDriverClassName);
    }

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductJpaRepository productJpaRepository;
    @Autowired private UserJpaRepository userJpaRepository;
    @Autowired private CouponJpaRepository couponJpaRepository;
    @Autowired private CouponIssueJpaRepository couponIssueJpaRepository;
    @Autowired private OrderJpaRepository orderJpaRepository;

    Long savedUserId;
    Long savedProductId;
    Long savedCouponId;

    @BeforeEach
    void setUp() {
        orderJpaRepository.deleteAll();
        couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
        productJpaRepository.deleteAll();
        userJpaRepository.deleteAll();

        // 사용자 생성
        UserEntity user = new UserEntity();
        user.setUserName("테스트유저");
        user.setBalance(new BigDecimal("100000")); // 충분한 잔액
        UserEntity savedUser = userJpaRepository.save(user);
        savedUserId = savedUser.getUserId();

        // 상품 생성
        ProductEntity product = new ProductEntity();
        product.setGoodsName("테스트상품");
        product.setPrice(new BigDecimal("10000"));
        product.setStock(10);
        ProductEntity savedProduct = productJpaRepository.save(product);
        savedProductId = savedProduct.getGoodsId();

        // 쿠폰 생성 및 발급
        CouponEntity coupon = new CouponEntity();
        coupon.setCouponType("DEDUCT");
        coupon.setCouponValue(new BigDecimal("3000"));
        coupon.setCount(100);
        CouponEntity savedCoupon = couponJpaRepository.save(coupon);
        savedCouponId = savedCoupon.getCouponId();

        CouponIssueEntity issue = new CouponIssueEntity();
        issue.setCouponId(savedCouponId);
        issue.setUserId(savedUserId);
        issue.setUseYn("N");
        couponIssueJpaRepository.save(issue);
    }

    @Test
    void 주문_성공() throws Exception {
        String body = """
            {
              "userId": %d,
              "goodsId": %d,
              "couponId": %d,
              "count": 2
            }
        """.formatted(savedUserId, savedProductId, savedCouponId);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        		.andDo(print())
                .andExpect(status().isOk());

        List<OrderEntity> orders = orderJpaRepository.findAll();
        assertThat(orders).hasSize(1);
        assertThat(orders.get(0).getUserId()).isEqualTo(savedUserId);
        assertThat(orders.get(0).getGoodsId()).isEqualTo(savedProductId);
    }

    @Test
    void 잔액부족으로_실패() throws Exception {
        // 사용자 잔액을 1000원으로 설정
        UserEntity user = userJpaRepository.findById(savedUserId).get();
        user.setBalance(new BigDecimal("1000"));
        userJpaRepository.save(user);

        String body = """
            {
              "userId": %d,
              "goodsId": %d,
              "couponId": %d,
              "count": 1
            }
        """.formatted(savedUserId, savedProductId, savedCouponId);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        		.andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void 존재하지_않는_상품으로_실패() throws Exception {
        String body = """
            {
              "userId": %d,
              "goodsId": 9999,
              "couponId": %d,
              "count": 1
            }
        """.formatted(savedUserId, savedCouponId);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        		.andDo(print())
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("상품 정보가 없습니다."));
    }

    @Test
    void 쿠폰_없을때_정상주문() throws Exception {
        String body = """
            {
              "userId": %d,
              "goodsId": %d,
              "count": 1
            }
        """.formatted(savedUserId, savedProductId);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
        		.andDo(print())
                .andExpect(status().isOk());

        List<OrderEntity> orders = orderJpaRepository.findAll();
        assertThat(orders).hasSize(1);
    }
}
