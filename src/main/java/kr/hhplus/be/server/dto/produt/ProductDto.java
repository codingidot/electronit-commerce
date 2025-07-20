package kr.hhplus.be.server.dto.produt;

import java.math.BigDecimal;

public class ProductDto {

    private Long goodsId;
    private String goodsName;
    private BigDecimal price;
    private Integer stock;
    private String goodsType;
    private Long repreGoodsId;

    public ProductDto(Long goodsId, String goodsName, BigDecimal price, Integer stock, String goodsType, Long repreGoodsId) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.price = price;
        this.stock = stock;
        this.goodsType = goodsType;
        this.repreGoodsId = repreGoodsId;
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
    }
}
