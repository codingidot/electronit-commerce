package kr.hhplus.be.server.coupon.entity;

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
    
    public CouponEntity(){};
    
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
	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public void setCouponName(String couponName) {
		this.couponName = couponName;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	public void setCouponValue(BigDecimal couponValue) {
		this.couponValue = couponValue;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
