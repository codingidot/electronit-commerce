package kr.hhplus.be.server.product.repository;

import java.util.List;
import java.util.Optional;

import kr.hhplus.be.server.product.dto.ProductRequest;
import kr.hhplus.be.server.product.entity.ProductEntity;

public interface ProductRepository {

	List<ProductEntity> findAll(ProductRequest param);
	Optional<ProductEntity> findById(Long goodsId);
	ProductEntity save(ProductEntity entity);
}
