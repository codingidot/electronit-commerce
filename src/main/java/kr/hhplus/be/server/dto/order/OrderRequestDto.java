package kr.hhplus.be.server.dto.order;

public class OrderRequestDto {

	Long GoodsId;
	int count;
	private Long couponId;
	
	public OrderRequestDto() {
		
	}
	
	public OrderRequestDto(Long goodsId, int count, Long couponId) {
		GoodsId = goodsId;
		this.count = count;
		this.couponId = couponId;
	}
	
	public Long getGoodsId() {
		return GoodsId;
	}
	public int getCount() {
		return count;
	}
	public Long getCouponId() {
		return couponId;
	} 
	
	
}
