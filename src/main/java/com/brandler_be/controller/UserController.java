package com.brandler_be.controller;

import com.brandler_be.dto.UserDto.req.Login;
import com.brandler_be.dto.UserDto.res.UserInfo;
import com.brandler_be.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * 사용자 관련 API 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "사용자", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;


    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody Login request) {
        userService.login(request);
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "유저 정보 불러오기")
    @GetMapping("/{email}")
    public ResponseEntity<UserInfo> getUserInfo(@PathVariable String email) {
        return ResponseEntity.ok(userService.getUserInfo(email));
    }


}
