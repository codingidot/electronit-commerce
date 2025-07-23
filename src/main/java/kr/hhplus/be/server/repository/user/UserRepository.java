package kr.hhplus.be.server.repository.user;

import java.util.Optional;

import kr.hhplus.be.server.dto.user.UserDto;

public interface UserRepository {
	
	Optional<UserDto> findById(Long id);
	void save(UserDto userDto);
}
