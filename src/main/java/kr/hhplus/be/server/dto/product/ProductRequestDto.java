package kr.hhplus.be.server.dto.product;


public class ProductRequestDto {

    private Long goodsId;
    private String goodsName;
    private String goodsType;
    
    public ProductRequestDto() {
    }
    
    public ProductRequestDto(Long goodsId, String goodsName, String goodsType) {
		this.goodsId = goodsId;
		this.goodsName = goodsName;
		this.goodsType = goodsType;
	}

	public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }
}
