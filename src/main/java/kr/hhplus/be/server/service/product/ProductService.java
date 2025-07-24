package kr.hhplus.be.server.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.dto.produt.ProductRequestDto;
import kr.hhplus.be.server.repository.product.ProductRepository;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<ProductDto> getProductList(ProductRequestDto requestDto) {
		return productRepository.findAll(requestDto);
	}

}
