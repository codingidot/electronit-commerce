package kr.hhplus.be.server.product.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
    
    public ProductEntity(){};

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
	
	public void deductStock(int cnt) {
		this.stock -= cnt;
	}

	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public void setGoodsType(String goodsType) {
		this.goodsType = goodsType;
	}

	public void setRepreGoodsId(Long repreGoodsId) {
		this.repreGoodsId = repreGoodsId;
	}
	
}
