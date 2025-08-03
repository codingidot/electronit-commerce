package kr.hhplus.be.server.user.repository;

import java.util.Optional;

import kr.hhplus.be.server.user.entity.UserEntity;

public interface UserRepository {
	
	Optional<UserEntity> findById(Long id);
	void save(UserEntity entity);
}
