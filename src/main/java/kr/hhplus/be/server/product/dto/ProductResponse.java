package kr.hhplus.be.server.product.dto;

import java.math.BigDecimal;

import kr.hhplus.be.server.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductResponse {
	
	private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
    private String goodsType;

	public static ProductResponse toDto(ProductEntity product) {
        return new ProductResponse(product.getGoodsId(), product.getGoodsName(), product.getPrice(), product.getStock(), product.getGoodsType());
    }
}
