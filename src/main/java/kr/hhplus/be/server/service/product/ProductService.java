package kr.hhplus.be.server.service.product;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.domain.product.Product;
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

}
