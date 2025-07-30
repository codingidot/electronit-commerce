package kr.hhplus.be.server.repository.product;

import java.util.List;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.entity.product.ProductEntity;

public interface ProductRepository {

	List<ProductEntity> findAll(ProductRequestDto param);
	void save(Product product);
}
