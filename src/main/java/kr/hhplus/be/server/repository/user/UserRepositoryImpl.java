package kr.hhplus.be.server.repository.user;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.entity.user.UserEntity;

@Repository
public class UserRepositoryImpl implements UserRepository{

	private final UserJpaRepository userJpaRepository;
	
	public UserRepositoryImpl(UserJpaRepository userJpaRepository) {
		super();
		this.userJpaRepository = userJpaRepository;
	}

	@Override
	public Optional<UserEntity> findById(Long id) {
		return userJpaRepository.findById(id);
	}

	@Override
	public void save(UserEntity entity) {
		userJpaRepository.save(entity);
	}

}
