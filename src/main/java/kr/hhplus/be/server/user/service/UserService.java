package kr.hhplus.be.server.user.service;


import java.math.BigDecimal;
import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.hhplus.be.server.user.dto.BalanceResponse;
import kr.hhplus.be.server.user.dto.ChargeRequest;
import kr.hhplus.be.server.user.dto.ChargeResponse;
import kr.hhplus.be.server.user.entity.UserEntity;
import kr.hhplus.be.server.user.repository.UserRepository;

@Service
public class UserService {
	
	private final UserRepository userRepository;
	private final RedisTemplate<Object, Object> redisTemplate;
	private static final String BALANCE_KEY_PREFIX = "balance:";
	
	UserService(UserRepository userRepository,RedisTemplate<Object, Object> redisTemplate){
		this.userRepository = userRepository;
		this.redisTemplate = redisTemplate;
	}
	
	//유저정보조회
	public UserEntity getUserInfo(Long userId) throws Exception {
		UserEntity user = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));
		return user;
	}
	
	//유저 잔액 조회
	public BalanceResponse getUserBalance(Long userId) throws Exception {
		
		String key = BALANCE_KEY_PREFIX + userId;
		
        BigDecimal cached = (BigDecimal) redisTemplate.opsForValue().get(key);
        if (cached != null) return new BalanceResponse(cached);

		UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new Exception("사용자를 찾을 수 없습니다."));

        redisTemplate.opsForValue().set(key, userEntity.getBalance(), Duration.ofSeconds(30));
		return new BalanceResponse(userEntity.getBalance());
	}

	//잔액충전
	@Transactional(rollbackFor = Exception.class)
	public ChargeResponse chargeBalance(ChargeRequest chargeRequestDto) throws Exception {
		Long userId = chargeRequestDto.getUserId();
		UserEntity entity =  this.getUserInfo(userId);
		entity.charge(chargeRequestDto.getAmount());
		userRepository.save(entity);
		String key = BALANCE_KEY_PREFIX + userId;
		
        redisTemplate.opsForValue().set(key, entity.getBalance(), Duration.ofSeconds(30));
		return ChargeResponse.toDto(entity.getBalance());
	}

	//유저정보 변경
	public void updateUser(UserEntity user) {
		userRepository.save(user);
	}

	//잔액차감
	public void deductBalance(UserEntity userInfo, BigDecimal totalPrice) throws Exception {
		if(userInfo == null) {
			throw new Exception("유저 정보가 존재하지 않습니다.");
		}
		UserEntity entity = userInfo;
		entity.deduct(totalPrice);
		userRepository.save(entity);
		String key = BALANCE_KEY_PREFIX + entity.getUserId();
		
        redisTemplate.opsForValue().set(key, entity.getBalance(), Duration.ofSeconds(30));
	}

}
