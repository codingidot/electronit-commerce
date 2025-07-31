package kr.hhplus.be.server.service.user;


import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.dto.user.ChargeRequestDto;
import kr.hhplus.be.server.dto.user.ChargeResponseDto;
import kr.hhplus.be.server.entity.user.UserEntity;
import kr.hhplus.be.server.repository.user.UserRepository;

public class UserService {
	
	private final UserRepository userRepository;
	
	UserService(UserRepository userRepository){
		this.userRepository = userRepository;
	}
	
	//유저정보조회
	public Optional<UserEntity> getUserInfo(Long userId) throws Exception {
		Optional<UserEntity> user = Optional.ofNullable(userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다.")));
		return user;
	}

	//잔액충전
	@Transactional(rollbackFor = Exception.class)
	public ChargeResponseDto chargeBalance(ChargeRequestDto chargeRequestDto) throws Exception {
		Long userId = chargeRequestDto.getUserId();
		Optional<UserEntity> user = this.getUserInfo(userId);
		UserEntity entity = user.get();
		entity.charge(chargeRequestDto.getAmount());
		userRepository.save(entity);
		return ChargeResponseDto.toDto(entity.getBalance());
	}

	//유저정보 변경
	public void updateUser(UserEntity user) {
		userRepository.save(user);
	}

}
