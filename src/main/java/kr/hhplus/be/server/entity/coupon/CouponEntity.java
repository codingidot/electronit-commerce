package kr.hhplus.be.server.entity.coupon;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.coupon.Coupon;

@Entity
public class CouponEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long couponId;
    private String couponName;
    private String couponType;
    private BigDecimal couponValue;
    private int count;
    
    CouponEntity(){};
    
    public Coupon toDomain(CouponEntity entity) {
    	return new Coupon(entity.getCouponId(), entity.getCouponName(), entity.getCouponType(), entity.getCouponValue(), false, entity.getCount());
    }
    
	public Long getCouponId() {
		return couponId;
	}
	public String getCouponName() {
		return couponName;
	}
	public String getCouponType() {
		return couponType;
	}
	public BigDecimal getCouponValue() {
		return couponValue;
	}
	public int getCount() {
		return count;
	}
}
