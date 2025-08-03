package kr.hhplus.be.server;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.product.repository.ProductJpaRepository;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ProductIntegrationTest {

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

    Long savedProductId;

    @BeforeEach
    void setUp() {
        productJpaRepository.deleteAll();

        ProductEntity product = new ProductEntity();
        product.setGoodsName("통합테스트상품");
        product.setGoodsType("ELECTRONICS");
        product.setPrice(new BigDecimal("12345"));
        product.setStock(20);

        ProductEntity saved = productJpaRepository.save(product);
        savedProductId = saved.getGoodsId();
    }

    @Test
    void 상품ID로_조회_성공() throws Exception {
        mockMvc.perform(get("/products")
                .param("goodsId", savedProductId.toString())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].goodsId").value(savedProductId))
                .andExpect(jsonPath("$.data[0].goodsName").value("통합테스트상품"));
    }

    @Test
    void 상품명으로_조회_성공() throws Exception {
        mockMvc.perform(get("/products")
                .param("goodsName", "통합테스트")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].goodsName").value("통합테스트상품"));
    }

    @Test
    void 상품타입으로_조회_성공() throws Exception {
        mockMvc.perform(get("/products")
                .param("goodsType", "ELECTRONICS")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].goodsType").value("ELECTRONICS"));
    }

    @Test
    void 상품조회_결과없음() throws Exception {
        mockMvc.perform(get("/products")
                .param("goodsName", "없는상품")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isEmpty());
    }
}
