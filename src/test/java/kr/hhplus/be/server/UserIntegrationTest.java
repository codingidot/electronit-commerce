package kr.hhplus.be.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import kr.hhplus.be.server.user.entity.UserEntity;
import kr.hhplus.be.server.user.repository.UserJpaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserIntegrationTest {

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

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserJpaRepository userJpaRepository;

    Long savedUserId;

    @BeforeEach
    void setUp() {
        userJpaRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUserName("테스트유저");
        user.setBalance(new BigDecimal("100000"));
        UserEntity saved = userJpaRepository.save(user);
        savedUserId = saved.getUserId();
    }

    @Test
    void 잔액조회_성공() throws Exception {
        mockMvc.perform(get("/wallet/balance/{userId}", savedUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.userId").value(savedUserId))
                .andExpect(jsonPath("$.data.balance").value(100000));
    }

    @Test
    void 잔액조회_존재하지않는_유저() throws Exception {
        mockMvc.perform(get("/wallet/balance/{userId}", 9999L))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("사용자를 찾을 수 없습니다."));
    }

    @Test
    void 잔액충전_성공() throws Exception {
        String requestBody = """
            {
                "userId": %d,
                "amount": 5000
            }
        """.formatted(savedUserId);

        mockMvc.perform(post("/wallet/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.code").value(200))
            .andExpect(jsonPath("$.data.balance").value(105000));

        UserEntity updatedUser = userJpaRepository.findById(savedUserId).get();
        assertThat(updatedUser.getBalance()).isEqualByComparingTo(new BigDecimal("105000"));
    }

    @Test
    void 잔액충전_존재하지않는_유저() throws Exception {
        String requestBody = """
            {
                "userId": 9999,
                "amount": 5000
            }
        """;

        mockMvc.perform(post("/wallet/charge")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value(500))
            .andExpect(jsonPath("$.message").value("잔액충전에 실패하였습니다."));
    }
}
