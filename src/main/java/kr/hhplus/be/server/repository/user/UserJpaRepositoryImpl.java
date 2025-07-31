package kr.hhplus.be.server.repository.user;

import java.util.Optional;

import kr.hhplus.be.server.entity.user.UserEntity;

public class UserJpaRepositoryImpl implements UserRepository{

	UserJpaRepository userJpaRepository;
	
	@Override
	public Optional<UserEntity> findById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public void save(UserEntity entity) {
		userJpaRepository.save(entity);
	}

}
