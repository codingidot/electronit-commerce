package kr.hhplus.be.server.product.dto;

import java.math.BigDecimal;

import kr.hhplus.be.server.product.entity.ProductEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProductResponseDto {
	
	private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
    private String goodsType;

	public static ProductResponseDto toDto(ProductEntity product) {
        return new ProductResponseDto(product.getGoodsId(), product.getGoodsName(), product.getPrice(), product.getStock(), product.getGoodsType());
    }
}
