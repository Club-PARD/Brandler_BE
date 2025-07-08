package com.brandler_be.service;

import com.brandler_be.domain.User;
import com.brandler_be.dto.UserDto.req.Login;
import com.brandler_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 로그인 처리
     * 이메일로 사용자를 조회하고,
     * 기존 사용자가 있을지 그냥 리턴
     * 없으면 새로 생성합니다.
     */
    @Transactional
    public void login(Login request) {
        log.info("로그인 요청 처리: {}", request.getEmail());
        
        // 이메일로 사용자 조회
        User user = userRepository.findByEmail(request.getEmail()).orElse(null);
        
        // 기존 사용자가 있으면 그냥 리턴
        if (user != null) {
            log.info("기존 사용자 발견: {}", user.getEmail());
            return;
        }
        
        // 새 사용자 생성
        log.info("새 사용자 생성: {}", request.getEmail());
        User newUser = User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .genre(request.getGenre())
                .build();
        userRepository.save(newUser);
    }



}
