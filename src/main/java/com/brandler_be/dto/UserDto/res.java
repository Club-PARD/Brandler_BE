package com.brandler_be.dto.UserDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class res {

    /**
     * 유저 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "유저 정보 응답")
    public static class UserInfo {
        
        @Schema(description = "유저 식별 이메일")
        private String email;
        
        @Schema(description = "닉네임")
        private String name;
        
        @Schema(description = "장르")
        private String genre;
    }
}
