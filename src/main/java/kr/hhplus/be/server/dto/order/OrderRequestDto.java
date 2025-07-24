package kr.hhplus.be.server.dto.order;

public class OrderRequestDto {

	Long userId;
	Long goodsId;
	int count;
	private Long couponId;
	
	public OrderRequestDto() {
		
	}
	
	public OrderRequestDto(Long userId ,Long goodsId, int count, Long couponId) {
		this.userId = userId;
		this.goodsId = goodsId;
		this.count = count;
		this.couponId = couponId;
	}
	
	public Long getUserId() {
		return userId;
	}
	public Long getGoodsId() {
		return goodsId;
	}
	public int getCount() {
		return count;
	}
	public Long getCouponId() {
		return couponId;
	} 
	
	
}
