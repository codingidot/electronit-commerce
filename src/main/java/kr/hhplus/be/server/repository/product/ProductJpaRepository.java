package kr.hhplus.be.server.repository.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.entity.product.ProductEntity;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long>{

	List<ProductEntity> findByGoodsNameContaining(String name);

	List<ProductEntity> findByGoodsType(String goodsType);

}
