package kr.hhplus.be.server.entity.coupon;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

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
