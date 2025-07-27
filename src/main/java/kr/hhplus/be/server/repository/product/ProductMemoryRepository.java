package kr.hhplus.be.server.repository.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import kr.hhplus.be.server.dto.product.Product;
import kr.hhplus.be.server.dto.product.ProductRequestDto;

public class ProductMemoryRepository implements ProductRepository{

	public Map<Long, Product> productTable = new HashMap<>();
	private Long productSeq = 1L;
	
	public ProductMemoryRepository(List<Product> list) {
		for(Product dto : list) {
			Long id = dto.getGoodsId();
			productTable.put(id, dto);
			if(id >= productSeq) {
				productSeq = id+1;
			}
		}
	}
	
	//상품조회
	@Override
	public List<Product> findAll(ProductRequestDto param) {
		List<Product> list = productTable.values().stream().filter(product -> param.getGoodsId() == null || product.getGoodsId().equals(param.getGoodsId()))
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
