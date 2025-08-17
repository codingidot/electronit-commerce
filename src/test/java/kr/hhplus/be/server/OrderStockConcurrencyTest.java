package kr.hhplus.be.server;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

import kr.hhplus.be.server.order.dto.OrderRequest;
import kr.hhplus.be.server.order.service.OrderFacade;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.product.repository.ProductJpaRepository;
import kr.hhplus.be.server.user.entity.UserEntity;
import kr.hhplus.be.server.user.repository.UserJpaRepository;

@SpringBootTest
@Testcontainers
@DisplayName("주문시 재고차감 동시성 테스트")
public class OrderStockConcurrencyTest {
	
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
    ProductJpaRepository productJpaRepository;
    
    @Autowired
    UserJpaRepository userJpaRepository;
    
    @Autowired
    OrderFacade orderFacade;

    ArrayList<Long> idList = new ArrayList<>();
    
    Long productId = 0L;
    
    @BeforeEach
    void setup() {
    	productJpaRepository.deleteAll();
    	userJpaRepository.deleteAll();
    	
    	//유저등록
        for(int id = 1; id <=200 ; id++) {
        	UserEntity user = new UserEntity();
        	user.setUserId(null);
        	user.setBalance(new BigDecimal(1000000));
        	user.setUserName("test user");
        	user = userJpaRepository.save(user);
        	idList.add(user.getUserId());
        }
        Collections.sort(idList);
        
        //상품등록
        ProductEntity product = new ProductEntity();
        product.setGoodsId(null);
        product.setGoodsName("test product");
        product.setGoodsType("N");//일반상품
        product.setPrice(new BigDecimal(100));
        product.setStock(300);
        productId = productJpaRepository.save(product).getGoodsId();
    }
    
    @Test
    @DisplayName("상품 주문시 재고 차감 동시성 테스트")
    void 동시에_상품_3개씩_200명이_사면_재고는_0() throws InterruptedException {

    	//given
    	ExecutorService executorService = Executors.newFixedThreadPool(10); // 스레드풀
	    CountDownLatch startLatch = new CountDownLatch(1); // 동시에 출발
	    CountDownLatch doneLatch = new CountDownLatch(200); // 작업 완료 대기
    	 
	    //when
	    for(Long id : idList) {
	    	OrderRequest request = new OrderRequest(id ,productId, 3, null);
	    	
	    	executorService.submit(() -> {
                try {
                	startLatch.await();
                	orderFacade.order(request);
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
        int stock = productJpaRepository.findById(1L).get().getStock();
        
        assertThat(stock).isEqualTo(0);
    }
}
