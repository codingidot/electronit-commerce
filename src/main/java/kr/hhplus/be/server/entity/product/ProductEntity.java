package kr.hhplus.be.server.entity.product;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.product.Product;

@Entity
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
    private String goodsType;
    private Long repreGoodsId;
    
    protected ProductEntity(){};
	private ProductEntity(Long goodsId, String goodsName, BigDecimal price, Integer stock, String goodsType,
			Long repreGoodsId) {
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.price = price;
		this.stock = stock;
		this.goodsType = goodsType;
		this.repreGoodsId = repreGoodsId;
	}

	public static ProductEntity toEntity(Product product) {
		return new ProductEntity(product.getGoodsId(), product.getGoodsName(), product.getPrice(), product.getStock(), product.getGoodsType(), product.getRepreGoodsId());
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

	public Long getRepreGoodsId() {
		return repreGoodsId;
	};
}
