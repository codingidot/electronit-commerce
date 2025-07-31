package kr.hhplus.be.server.service.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.dto.product.ProductResponseDto;
import kr.hhplus.be.server.entity.product.ProductEntity;
import kr.hhplus.be.server.repository.product.ProductRepository;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	//상품리스트정보 가져오기
	public List<ProductResponseDto> getProductList(ProductRequestDto requestDto) {
		return productRepository.findAll(requestDto).stream().map(ProductResponseDto::toDto).collect(Collectors.toList());
	}

	//주문하는 상품정보 가져오기
	public Optional<ProductEntity> getOrderProductInfo(Long goodsId) throws Exception {
		return productRepository.findById(goodsId);
	}

	//재고차감
	public void deductStock(Optional<ProductEntity> productEntity, int count) throws Exception {
		if(productEntity.isEmpty()) {
			throw new Exception("상품 정보가 없습니다.");
		}
		ProductEntity entity = productEntity.get();
		entity.deductStock(count);
		productRepository.save(entity);
	}

}
