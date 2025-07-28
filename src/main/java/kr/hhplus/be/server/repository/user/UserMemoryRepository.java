package kr.hhplus.be.server.repository.user;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import kr.hhplus.be.server.domain.user.User;

@Repository
public class UserMemoryRepository implements UserRepository{
	public Map<Long, User> userTable = new HashMap<>();
	
	@Override
	public Optional<User> findById(Long id){
		return Optional.ofNullable(userTable.get(id));
	}

	@Override
	public void save(User userDto) {
		Long id = userDto.getUserId();
		userTable.put(id, userDto);
	}

}
