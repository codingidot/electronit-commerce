package kr.hhplus.be.server.service.product;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.hhplus.be.server.dto.product.Product;
import kr.hhplus.be.server.dto.product.ProductRequestDto;
import kr.hhplus.be.server.repository.product.ProductRepository;

@Service
public class ProductService {
	
	private ProductRepository productRepository;
	
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	//상품리스트정보 가져오기
	public List<Product> getProductList(ProductRequestDto requestDto) {
		return productRepository.findAll(requestDto);
	}

	//주문하는 상품정보 가져오기
	public Product getOrderProductInfo(Long goodsId) throws Exception {
		ProductRequestDto param = new ProductRequestDto(goodsId, null, null);
		List<Product> list = this.getProductList(param);
		System.out.println("sssdd  " + list.size());
		if(list.size() == 0) {
			throw new Exception("해당 상품은 존재하지 않습니다.");
		}
		
		return list.get(0);
	}

}
