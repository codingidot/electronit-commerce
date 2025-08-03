package kr.hhplus.be.server.dto.product;

import java.math.BigDecimal;

import kr.hhplus.be.server.entity.product.ProductEntity;

public class ProductResponseDto {
	
	private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
    private String goodsType;

    private ProductResponseDto(Long goodsId, String goodsName, BigDecimal price, Integer stock, String goodsType) {
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.price = price;
		this.stock = stock;
		this.goodsType = goodsType;
	}

	public static ProductResponseDto toDto(ProductEntity product) {
        return new ProductResponseDto(product.getGoodsId(), product.getGoodsName(), product.getPrice(), product.getStock(), product.getGoodsType());
    }

	public Long getGoodsId() {
		return goodsId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public Integer getStock() {
		return stock;
	}

	public String getGoodsType() {
		return goodsType;
	}
}
