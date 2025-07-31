package kr.hhplus.be.server.repository.product;

import java.util.List;
import java.util.Optional;

import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.entity.product.ProductEntity;

public interface ProductRepository {

	List<ProductEntity> findAll(ProductRequestDto param);
	Optional<ProductEntity> findById(Long goodsId);
	void save(ProductEntity entity);
}
