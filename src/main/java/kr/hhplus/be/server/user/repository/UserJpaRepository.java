package kr.hhplus.be.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.hhplus.be.server.user.entity.UserEntity;

public interface UserJpaRepository extends JpaRepository<UserEntity , Long>{

}
