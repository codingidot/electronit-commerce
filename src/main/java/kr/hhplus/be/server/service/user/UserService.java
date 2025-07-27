package kr.hhplus.be.server.service.user;


import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.dto.user.ChargeRequestDto;
import kr.hhplus.be.server.dto.user.User;
import kr.hhplus.be.server.repository.user.UserRepository;

public class UserService {
	
	private final UserRepository userRepository;
	
	UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	//잔액조회
	public User getUserInfo(Long userId) throws Exception {
		User userDto = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
		return userDto;
	}

	//잔액충전
	@Transactional(rollbackFor = Exception.class)
	public User chargeBalance(ChargeRequestDto chargeRequestDto) throws Exception {
		Long userId = chargeRequestDto.getUserId();
		User userDto = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
		userDto.charge(chargeRequestDto.getAmount());
		userRepository.save(userDto);
		return userDto;
	}

	//유저정보 변경
	public void updateUser(User user) {
		userRepository.save(user);
	}

}
