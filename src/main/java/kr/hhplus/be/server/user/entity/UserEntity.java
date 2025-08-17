package kr.hhplus.be.server.user.entity;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;
    private String userName;
    private BigDecimal balance;
    
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
}
