package com.brandler_be.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class req {

    /**
     * 로그인 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "로그인 요청 정보")
    public static class Login {
        
        @Schema(description = "유저 식별 이메일")
        private String email;
        
        @Schema(description = "닉네임")
        private String name;
        
        @Schema(description = "장르")
        private String genre;
    }
    
    /**
     * 유저 정보 수정 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "유저 정보 수정 요청")
    public static class UpdateUserInfo {
        
        @Schema(description = "닉네임")
        private String name;
        
        @Schema(description = "장르")
        private String genre;
    }
}
