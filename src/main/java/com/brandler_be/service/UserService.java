package com.brandler_be.service;

import com.brandler_be.domain.User;
import com.brandler_be.dto.UserDto.req;
import com.brandler_be.dto.UserDto.res;
import com.brandler_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

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
    public void login(req.Login request) {
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


    /**
     * 이메일로 유저 정보 조회
     */
    @Transactional(readOnly = true)
    public res.UserInfo getUserInfo(String email) {
        log.info("유저 정보 조회: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        return res.UserInfo.builder()
                .email(user.getEmail())
                .name(user.getName())
                .genre(user.getGenre())
                .build();
    }


    /**
     * 유저 정보 수정
     */
    @Transactional
    public void updateUserInfo(String email, req.UpdateUserInfo request) {
        log.info("유저 정보 수정: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        user.updateUserInfo(request.getName(), request.getGenre());
        userRepository.save(user);
    }


}
