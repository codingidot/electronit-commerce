package kr.hhplus.be.server.coupon.entity;

import java.util.Optional;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CouponIssueEntity {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long issueId;

    private Long couponId;

    private Long userId;

    private String useYn;

    public CouponIssueEntity() {}
    
    private CouponIssueEntity(Long issuedId, Long couponId, Long userId, String useYn) {
    	this.issueId = issuedId;
    	this.couponId = couponId;
    	this.userId = userId;
    	this.useYn = useYn;
    }
    
    public static CouponIssueEntity toEntity(Optional<CouponEntity> cpEntity, int userIssueCnt, Long userId) throws Exception {
    	CouponEntity coupon;
    	if(cpEntity.isEmpty()) {
        	throw new Exception("해당 쿠폰은 존재하지 않습니다.");
        }else {
        	coupon = cpEntity.get();
        	int issuedCnt = coupon.getIssuedCount();
        	coupon = cpEntity.get();
        	int limit = coupon.getCount();
        	Long couponId = coupon.getCouponId();
        	
        	if(issuedCnt >= limit) {
    			throw new Exception("해당 쿠폰은 선착순 마감되었습니다.");
    		}
        	
        	if(userIssueCnt > 0 ) {
        		//throw new Exception("이미 발급받은 쿠폰입니다.");
        	}
        }
        return new CouponIssueEntity(null,coupon.getCouponId(),userId, "N");
    }

	public String getUseYn() {
		return useYn;
	}

	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}

	public Long getIssueId() {
		return issueId;
	}

	public Long getCouponId() {
		return couponId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setIssueId(Long issueId) {
		this.issueId = issueId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
}
