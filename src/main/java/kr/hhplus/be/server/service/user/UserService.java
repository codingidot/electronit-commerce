package kr.hhplus.be.server.service.user;


import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.dto.user.ChargeRequestDto;
import kr.hhplus.be.server.dto.user.UserDto;
import kr.hhplus.be.server.repository.user.UserRepository;

public class UserService {
	
	private final UserRepository userRepository;
	
	UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	//잔액조회
	public UserDto getBalanceInfo(Long userId) throws Exception {
		UserDto userDto = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
		return userDto;
	}

	//잔액충전
	@Transactional(rollbackFor = Exception.class)
	public UserDto chargeBalance(ChargeRequestDto chargeRequestDto) throws Exception {
		Long userId = chargeRequestDto.getUserId();
		UserDto userDto = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
		userDto.charge(chargeRequestDto.getAmount());
		userRepository.save(userDto);
		return userDto;
	}

}
