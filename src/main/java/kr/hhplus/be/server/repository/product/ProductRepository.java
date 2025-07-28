package kr.hhplus.be.server.repository.product;

import java.util.List;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.dto.product.ProductRequestDto;

public interface ProductRepository {

	List<Product> findAll(ProductRequestDto param);
	void save(Product productDto);
}
