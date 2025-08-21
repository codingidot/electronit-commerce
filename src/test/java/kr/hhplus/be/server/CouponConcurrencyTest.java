package kr.hhplus.be.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import kr.hhplus.be.server.coupon.dto.CouponIssueRequest;
import kr.hhplus.be.server.coupon.entity.CouponEntity;
import kr.hhplus.be.server.coupon.repository.CouponIssueJpaRepository;
import kr.hhplus.be.server.coupon.repository.CouponJpaRepository;
import kr.hhplus.be.server.coupon.repository.CouponRepository;
import kr.hhplus.be.server.coupon.service.CouponService;

@SpringBootTest
@Testcontainers
@DisplayName("쿠폰 동시 발급 테스트")
class CouponConcurrencyTest {
	
	@Container
    static MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("hhplus")
            .withUsername("root")
            .withPassword("1234");
    
    static {
    	mysqlContainer.start(); // ✅ 컨테이너를 먼저 시작
    }

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "com.mysql.cj.jdbc.Driver");
        
    }

    @Autowired
    private CouponService couponService; // 위의 issueCoupon 메서드가 있는 서비스

    @Autowired
    private CouponRepository couponRepository;
    
    @Autowired
    private CouponIssueJpaRepository couponIssueJpaRepository;
    
    @Autowired
    CouponJpaRepository couponJpaRepository;

    private final int THREAD_COUNT = 200;
    private Long testCouponId;

    @BeforeEach
    void setup() {
    	couponIssueJpaRepository.deleteAll();
        couponJpaRepository.deleteAll();
        
        CouponEntity coupon = new CouponEntity();
        coupon.setCount(100);
        coupon.setCouponId(null);
        coupon.setCouponName("test coupon");
        coupon.setCouponType("PERCENT");
        
        testCouponId = couponJpaRepository.saveAndFlush(coupon).getCouponId();

    }
 


    @Test
    @DisplayName("쿠폰 발급시 사용자가 쿠폰을 중복 발급하는지, 총 발급된 쿠폰수가 쿠폰 선착순 수 이내인지 테스트")
    void 동시에_쿠폰_200개_요청하면_100개만_발급되어야_한다() throws InterruptedException {
    	//given
    	ExecutorService executorService = Executors.newFixedThreadPool(10); // 스레드풀
	    CountDownLatch startLatch = new CountDownLatch(1); // 동시에 출발
	    CountDownLatch doneLatch = new CountDownLatch(THREAD_COUNT); // 작업 완료 대기
    	 
	    //when
        for (int i = 0; i < THREAD_COUNT; i++) {
            final Long userId = (long) i; // 서로 다른 사용자
            executorService.submit(() -> {
                try {
                	startLatch.await();
                    couponService.issueCoupon(new CouponIssueRequest(userId, testCouponId));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    doneLatch.countDown();
                }
            });
        }

        startLatch.countDown(); // 동시에 시작
        doneLatch.await();      // 전부 끝날 때까지 대기
        
        //then
        int issueCnt = 0;
        for (int i = 0; i < THREAD_COUNT; i++) {
        	int cnt = couponRepository.checkUserCouponHave(testCouponId, (long) i);

        	if(cnt > 1) {
        		fail("사용자 " + i + "에게 쿠폰이 중복 발급되었습니다.");
        	}else if (cnt == 1) {
        		issueCnt++;
        	}
        }
        assertThat(issueCnt).isEqualTo(100);
    }
}
