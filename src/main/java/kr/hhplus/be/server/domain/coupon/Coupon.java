package kr.hhplus.be.server.domain.coupon;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;

@Entity
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long couponId;
    private String couponName;
    private String couponType;
    private BigDecimal couponValue;
    private int count;
    @Transient
    private boolean issueSuccess;
    
	public Coupon(Long couponId, String couponName, String couponType, BigDecimal couponValue, boolean issueSuccess, int count) {
		this.couponId = couponId;
		this.couponName = couponName;
		this.couponType = couponType;
		this.couponValue = couponValue; 
		this.issueSuccess = issueSuccess;
		this.count = count;
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

	public boolean isIssueSuccess() {
		return issueSuccess;
	}
	
	public int getCount() {
		return count;
	}
}
