package kr.hhplus.be.server.repository.user;

import java.util.Optional;

import kr.hhplus.be.server.dto.user.User;

public interface UserRepository {
	
	Optional<User> findById(Long id);
	void save(User userDto);
}
