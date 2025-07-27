package kr.hhplus.be.server.repository.product;

import java.util.List;

import kr.hhplus.be.server.dto.product.Product;
import kr.hhplus.be.server.dto.product.ProductRequestDto;

public interface ProductRepository {

	List<ProductDto> findAll(ProductRequestDto param);
	void save(ProductDto productDto);
}
