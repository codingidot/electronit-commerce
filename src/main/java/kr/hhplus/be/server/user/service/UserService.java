package kr.hhplus.be.server.user.service;


import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.user.dto.ChargeRequestDto;
import kr.hhplus.be.server.user.dto.ChargeResponseDto;
import kr.hhplus.be.server.user.entity.UserEntity;
import kr.hhplus.be.server.user.repository.UserRepository;

@Service
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

	//잔액차감
	public void deductBalance(Optional<UserEntity> userInfo, BigDecimal totalPrice) throws Exception {
		if(userInfo.isEmpty()) {
			throw new Exception("유저 정보가 존재하지 않습니다.");
		}
		UserEntity entity = userInfo.get();
		entity.deduct(totalPrice);
		userRepository.save(entity);
	}

}
