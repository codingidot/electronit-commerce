package kr.hhplus.be.server.repository.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.hhplus.be.server.dto.produt.ProductDto;
import kr.hhplus.be.server.dto.produt.ProductRequestDto;

public class ProductMemoryRepository implements ProductRepository{

	public Map<Long, ProductDto> productTable = new HashMap<>();
	private Long productSeq = 1L;
	
	public ProductMemoryRepository(List<ProductDto> list) {
		for(ProductDto dto : list) {
			Long id = dto.getGoodsId();
			productTable.put(id, dto);
			if(id >= productSeq) {
				productSeq = id+1;
			}
		}
	}
	
	//상품조회
	@Override
	public List<ProductDto> findAll(ProductRequestDto param) {
		List<ProductDto> list = productTable.values().stream().filter(product -> param.getGoodsId() == null || product.getGoodsId().equals(param.getGoodsId()))
															  .filter(product -> param.getGoodsName() == null || product.getGoodsName().contains(param.getGoodsName()))
															  .filter(product -> param.getGoodsType() == null || product.getGoodsType().equals(param.getGoodsType()))
															  .collect(Collectors.toList());
		return list;
	}

	//상품정보 update
	@Override
	public void save(ProductDto productDto) {
		productTable.put(productDto.getGoodsId(), productDto);
	}
	
	//

}
