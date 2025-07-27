package kr.hhplus.be.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.hhplus.be.server.dto.user.ChargeRequestDto;
import kr.hhplus.be.server.dto.user.User;
import kr.hhplus.be.server.repository.user.UserRepository;
import kr.hhplus.be.server.service.user.UserService;

@ExtendWith(MockitoExtension.class)
public class UserBalanceTest {
	
	@Mock
	UserRepository userRepository;
	
	@InjectMocks
	UserService userService;
	
	private User testUser;
	
	@BeforeEach
	void setup() {
		testUser = new User(1L, "testUser", new BigDecimal(1000));
	}
	
	@Test
	@DisplayName("충전성공 테스트")
	public void chargeSuccessTest() throws Exception {
		//given
		Long id = 1L;
		BigDecimal chargeAmount = new BigDecimal(500);
		ChargeRequestDto chargeRequest = new ChargeRequestDto(id, chargeAmount);
		
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(testUser));

		//when
		User result = userService.chargeBalance(chargeRequest);
		
		//then
		assertNotEquals(result.getBalance(), testUser.getBalance().add(chargeAmount));
	}
	
	@Test
	@DisplayName("충전하는 유저 아이디에 해당하는 유저가 없을때")
	public void whenChargeNoUserTest() {
		//given
		Long id = 99L;
		BigDecimal chargeAmount = new BigDecimal(500);
		ChargeRequestDto chargeRequest = new ChargeRequestDto(id, chargeAmount);
		
		when(userRepository.findById(99L)).thenReturn(Optional.empty());
		
		//when
		Exception exception = assertThrows(Exception.class, ()-> userService.chargeBalance(chargeRequest));
		
		//then
		 assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
	}
	
	@Test
	@DisplayName("충전하는 금액이 음수인 경우")
	public void chargeMinusExceptionTest() {
		//given
		Long id = 1L;
		BigDecimal chargeAmount = new BigDecimal(-1000);
		ChargeRequestDto chargeRequest = new ChargeRequestDto(id, chargeAmount);
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(testUser));
		
		//when
		Exception exception = assertThrows(Exception.class, () -> userService.chargeBalance(chargeRequest));
		
		//then
		assertEquals("충전 금액은 0보다 커야 합니다.", exception.getMessage());
		
	}
	
	@Test
	@DisplayName("충전된 금액이 천만원이 넘는 경우")
	public void chargeRangeExceptionTest() {
		//given
		Long id = 1L;
		BigDecimal chargeAmount = new BigDecimal(10000000);
		ChargeRequestDto chargeRequest = new ChargeRequestDto(id, chargeAmount);
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(testUser));
		
		//when
		Exception exception = assertThrows(Exception.class, () -> userService.chargeBalance(chargeRequest));
		
		//then
		assertEquals("총 잔액은 1,000만원을 넘을 수 없습니다.", exception.getMessage());	
	}

	@Test
	@DisplayName("잔액 조회")
	public void selectBalance() throws Exception {
		//given
		Long id = 1L;
		when(userRepository.findById(id)).thenReturn(Optional.ofNullable(testUser));
		
		//when
		User result = userService.getUserInfo(id);
		
		//then
		assertEquals(testUser.getBalance(), result.getBalance());
	}
	
	@Test
	@DisplayName("잔액 조회시 해당 id 유저가 없는경우")
	public void selectBalanceWithWrongId() {
		//given
		Long id = 77L;
		when(userRepository.findById(id)).thenReturn(Optional.empty());
		
		//when
		Exception exception = assertThrows(Exception.class, ()-> userService.getUserInfo(id));
		
		//then
		assertEquals("사용자를 찾을 수 없습니다.", exception.getMessage());
	}
}
