package kr.hhplus.be.server.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.entity.user.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity , Long>{

}
