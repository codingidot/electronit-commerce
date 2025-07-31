package kr.hhplus.be.server.repository.product;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.entity.product.ProductEntity;

@Repository
public class ProductRepositoryImpl implements ProductRepository{

	private final ProductJpaRepository productRepository;

    public ProductRepositoryImpl(ProductJpaRepository productRepository) {
        this.productRepository = productRepository;
    }
	
	@Override
    public List<ProductEntity> findAll(ProductRequestDto param) {
        Long goodsId = param.getGoodsId();
        String name = param.getGoodsName();
        String goodsType = param.getGoodsType();

        List<ProductEntity> results = new ArrayList<>();

        if (goodsId != null) {
            // ID로 단건 조회 후 type 체크
            Optional<ProductEntity> entity = productRepository.findById(goodsId);
            if(entity.isPresent()) {
            	results.add(entity.get());
            }
        } else if (name != null) {
            results = productRepository.findByGoodsNameContaining(name);
        } else if (goodsType != null) {
            // type만 있을 때
            results = productRepository.findByGoodsType(goodsType);
        }

        return results;
    }

	@Override
	public void save(ProductEntity entity) {
		productRepository.save(entity);
	}

	@Override
	public Optional<ProductEntity> findById(Long goodsId) {
		return productRepository.findById(goodsId);
	}
}
