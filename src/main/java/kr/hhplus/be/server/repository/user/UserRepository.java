package kr.hhplus.be.server.repository.user;

import java.util.Optional;

import kr.hhplus.be.server.entity.user.UserEntity;

public interface UserRepository {
	
	Optional<UserEntity> findById(Long id);
	void save(UserEntity entity);
}
