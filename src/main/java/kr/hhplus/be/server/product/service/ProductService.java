package kr.hhplus.be.server.product.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import kr.hhplus.be.server.product.dto.ProductRequest;
import kr.hhplus.be.server.product.dto.ProductResponse;
import kr.hhplus.be.server.product.entity.ProductEntity;
import kr.hhplus.be.server.product.repository.ProductRepository;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	private final StringRedisTemplate redisTemplate;
	
	public ProductService(ProductRepository productRepository, StringRedisTemplate redisTemplate) {
		this.productRepository = productRepository;
		this.redisTemplate = redisTemplate;
	}

	//상품리스트정보 가져오기
	public List<ProductResponse> getProductList(ProductRequest requestDto) {
		return productRepository.findAll(requestDto).stream().map(ProductResponse::toDto).collect(Collectors.toList());
	}

	//주문하는 상품정보 가져오기
	public Optional<ProductEntity> getOrderProductInfo(Long goodsId) throws Exception {
		return productRepository.findById(goodsId);
	}

	//재고차감
	public ProductEntity deductStock(Optional<ProductEntity> productEntity, int count) throws Exception {
		if(productEntity.isEmpty()) {
			throw new Exception("상품 정보가 없습니다.");
		}
		ProductEntity entity = productEntity.get();
		entity.deductStock(count);
		entity = productRepository.save(entity);
		return entity;
	}
	
	//3일간 가장 많이 주문한 상품조회
	public List<String> getTop3Products() {
		String destKey = "product:rank:3days:tmp";

	    // 캐시에 이미 있으면 바로 리턴
	    if (Boolean.TRUE.equals(redisTemplate.hasKey(destKey))) {
	        Set<String> cached = redisTemplate.opsForZSet().reverseRange(destKey, 0, 2);
	        return cached == null ? List.of() : new ArrayList<>(cached);
	    }

	    // 없으면 새로 계산
	    LocalDate today = LocalDate.now();
	    String key1 = "product:rank:" + today.minusDays(2);
	    String key2 = "product:rank:" + today.minusDays(1);
	    String key3 = "product:rank:" + today;

	    redisTemplate.execute((RedisCallback<Object>) connection -> {
	        connection.zUnionStore(destKey.getBytes(),
	                new byte[][]{key1.getBytes(), key2.getBytes(), key3.getBytes()});
	        // TTL 10분
	        connection.expire(destKey.getBytes(), 600);
	        return null;
	    });

	    Set<String> topProducts = redisTemplate.opsForZSet().reverseRange(destKey, 0, 2);
	    return topProducts == null ? List.of() : new ArrayList<>(topProducts);
    }

}
