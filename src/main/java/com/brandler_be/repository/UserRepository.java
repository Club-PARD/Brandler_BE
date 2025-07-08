package com.brandler_be.repository;

import com.brandler_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    
    //이메일로 사용자 조회
    Optional<User> findByEmail(String email);


}
