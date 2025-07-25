package com.brandler_be.dto.ScrapDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class res {

    /**
     * 스크랩 여부 확인 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "스크랩 여부 확인 응답")
    public static class ScrapStatus {
        
        @Schema(description = "스크랩 여부", example = "true")
        private Boolean isScraped;
    }
    
    /**
     * 스크랩 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "스크랩 정보 응답")
    public static class ScrapInfo {
        
        @Schema(description = "브랜드 ID")
        private int brandId;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "브랜드 로고")
        private String brandLogo;
        
        @Schema(description = "브랜드 배너")
        private String brandBanner;
        
        @Schema(description = "브랜드 슬로건")
        private String slogan;
    }
    
    /**
     * 테스트용 스크랩 결과 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "테스트용 스크랩 결과")
    public static class TestScrapResult {
        
        @Schema(description = "브랜드 ID")
        private int brandId;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "설정된 스크랩 수")
        private int scrapCount;
        
        @Schema(description = "생성된 테스트 사용자 수")
        private int createdUsers;
    }
}
