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
    
    /**
     * 브랜드 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "브랜드 정보 응답")
    public static class BrandInfo {
        
        @Schema(description = "브랜드 ID")
        private Long id;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "브랜드 로고 이미지")
        private String brandLogo;
        
        @Schema(description = "브랜드 배너 이미지")
        private String brandBanner;
        
        @Schema(description = "브랜드 설명")
        private String description;
        
        @Schema(description = "브랜드 공식 웹사이트 URL")
        private String brandPageUrl;
        
        @Schema(description = "브랜드 장르")
        private String genre;
    }
}
