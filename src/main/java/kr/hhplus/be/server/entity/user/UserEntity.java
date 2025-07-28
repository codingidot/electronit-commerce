package kr.hhplus.be.server.entity.user;

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
    
    UserEntity(){};
    
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
