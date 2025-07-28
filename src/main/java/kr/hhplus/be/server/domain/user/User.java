package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;

public class User {

	private Long userId;
    private String userName;
    private BigDecimal balance;
    
    public User(Long userId, String userName, BigDecimal balance){
    	this.userId = userId;
    	this.userName = userName;
    	this.balance = balance;
    }
    
	public Long getUserId() {
		return userId;
	}
	public String getUserName() {
		return userName;
	}
	public BigDecimal getBalance() {
		return balance;
	}

	public void charge(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("충전 금액은 0보다 커야 합니다.");
        }
        if (this.balance.add(amount).compareTo(new BigDecimal("10000000")) > 0) {
            throw new IllegalArgumentException("총 잔액은 1,000만원을 넘을 수 없습니다.");
        }
        this.balance = this.balance.add(amount);
    }
}
