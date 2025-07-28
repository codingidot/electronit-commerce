package kr.hhplus.be.server;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Assertions;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.repository.product.ProductMemoryRepository;
import kr.hhplus.be.server.service.product.ProductService;

public class UnitTest {
	
	ProductMemoryRepository productRepository;
	ProductService productService;
	
	@BeforeEach
	public void testSetting() {
		List<Product> productList = new ArrayList<>();
		productList.add(new Product(1L, "상품A", new BigDecimal("19900"), 50, "N", null));
        productList.add(new Product(2L, "상품B", new BigDecimal("29900"), 30, "N", null));
        productList.add(new Product(3L, "상품C", new BigDecimal("15900"), 100, "O", 4L));
        productList.add(new Product(4L, "상품D", new BigDecimal("45900"), 10, "R", null));
        
		productRepository = new ProductMemoryRepository(productList);
		productService = new ProductService(productRepository);
		
		
	}

	@Test
	@DisplayName("상품 검색 테스트")
	public void selectProductListTest() {
		ProductRequestDto param1 = new ProductRequestDto(2L, null, null);//상품 아이디 검색
		ProductRequestDto param2 = new ProductRequestDto(null, "C", null);//상품이름 like 검색
		ProductRequestDto param3 = new ProductRequestDto(null, null, "N");//상품타입 검색
		ProductRequestDto param4 = new ProductRequestDto(4L, "D", "R");//종합검색
				
		List<Product> list1 = productService.getProductList(param1);
		List<Product> list2 = productService.getProductList(param2);
		List<Product> list3 = productService.getProductList(param3);
		List<Product> list4 = productService.getProductList(param4);
		
		//아이디 검색 검증
		Assertions.assertThat(list1)
			.hasSize(1)
			.extracting("goodsId", "goodsName", "goodsType")
			.containsExactly(Tuple.tuple(2L, "상품B", "N"));

		//상품명 검색 검증
		Assertions.assertThat(list2)
			.hasSize(1)
			.extracting("goodsId", "goodsName", "goodsType")
			.containsExactly(Tuple.tuple(3L, "상품C", "O"));

		//상품타입 검색 검증
		Assertions.assertThat(list3)
			.hasSize(2)
			.extracting("goodsId", "goodsName", "goodsType")
			.containsExactly(Tuple.tuple(1L, "상품A", "N"), Tuple.tuple(2L, "상품B", "N"));
		
		//통합 검색 검증
		Assertions.assertThat(list4)
			.hasSize(1)
			.extracting("goodsId", "goodsName", "goodsType")
			.containsExactly(Tuple.tuple(4L, "상품D", "R"));
	}
}
