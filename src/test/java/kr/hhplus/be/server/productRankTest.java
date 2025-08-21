package kr.hhplus.be.server;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import kr.hhplus.be.server.product.service.ProductService;


@SpringBootTest
@ActiveProfiles("test")
public class productRankTest {
	@Autowired
    private ProductService productService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private String key1;
    private String key2;
    private String key3;

    @BeforeEach
    void setUp() {
        // 최근 3일치 키 세팅
        LocalDate today = LocalDate.now();
        key1 = "product:rank:" + today.minusDays(2);
        key2 = "product:rank:" + today.minusDays(1);
        key3 = "product:rank:" + today;

        // Redis 초기화
        redisTemplate.delete(List.of(key1, key2, key3, "product:rank:3days:tmp"));

        // 더미 데이터 넣기 (상품 ID는 문자열)
        // 상품 101: 총합 15
        redisTemplate.opsForZSet().incrementScore(key1, "101", 5);
        redisTemplate.opsForZSet().incrementScore(key2, "101", 10);

        // 상품 102: 총합 8
        redisTemplate.opsForZSet().incrementScore(key2, "102", 3);
        redisTemplate.opsForZSet().incrementScore(key3, "102", 5);

        // 상품 103: 총합 20
        redisTemplate.opsForZSet().incrementScore(key1, "103", 7);
        redisTemplate.opsForZSet().incrementScore(key2, "103", 8);
        redisTemplate.opsForZSet().incrementScore(key3, "103", 5);

        // 상품 104: 총합 2
        redisTemplate.opsForZSet().incrementScore(key3, "104", 2);
    }

    @Test
    void 최근3일간_가장많이팔린상품_TOP3_조회() {
        // when
        List<String> top3 = productService.getTop3Products();

        // then
        assertThat(top3).hasSize(3);
        assertThat(top3.get(0)).isEqualTo("103"); // 가장 많이 팔린 상품
        assertThat(top3.get(1)).isEqualTo("101");
        assertThat(top3.get(2)).isEqualTo("102"); // 3등
    }

}
