package kr.hhplus.be.server.repository.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.dto.user.UserDto;

@Repository
public class UserMemoryRepository implements UserRepository{
	public Map<Long, UserDto> userTable = new HashMap<>();
	
	@Override
	public Optional<UserDto> findById(Long id){
		return Optional.ofNullable(userTable.get(id));
	}

	@Override
	public void save(UserDto userDto) {
		Long id = userDto.getUserId();
		userTable.put(id, userDto);
	}

}
