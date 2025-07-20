package kr.hhplus.be.server.dto.balance;

import java.math.BigDecimal;

public class BalanceDto {

	private Long userId;
    private String userName;
    private BigDecimal balance;
    
    public BalanceDto(Long userId, String userName, BigDecimal balance){
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
}
