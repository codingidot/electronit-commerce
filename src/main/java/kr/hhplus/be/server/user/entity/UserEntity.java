package kr.hhplus.be.server.user.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
    private String userName;
    private BigDecimal balance;
    
    public UserEntity(){};

	public Long getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void charge(BigDecimal amount) throws Exception {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("충전 금액은 0보다 커야 합니다.");
        }
        if (this.balance.add(amount).compareTo(new BigDecimal("10000000")) > 0) {
            throw new Exception("총 잔액은 1,000만원을 넘을 수 없습니다.");
        }
        this.balance = this.balance.add(amount);
	}
	public void deduct(BigDecimal amount) throws Exception {
		BigDecimal remain = this.balance.subtract(amount);
		if(this.balance.subtract(amount).compareTo(BigDecimal.ZERO)<0) {
			throw new Exception("잔액이 부족합니다.");
		}
        this.balance = remain;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	
}
