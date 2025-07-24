package kr.hhplus.be.server.repository.product;

import java.util.List;

import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.dto.produt.ProductRequestDto;

public interface ProductRepository {
	List<ProductDto> findAll(ProductRequestDto param);
	void save(ProductDto productDto);
}
