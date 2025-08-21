package kr.hhplus.be.server.coupon.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class CouponEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long couponId;
    private String couponName;
    private String couponType;
    private BigDecimal couponValue;
    private int count;
    private int issuedCount;

}
