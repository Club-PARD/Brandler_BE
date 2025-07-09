package com.brandler_be.dto.BrandDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class res {

    /**
     * 최근 방문한 브랜드 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "최근 방문한 브랜드 정보")
    public static class RecentVisitInfo {
        
        @Schema(description = "브랜드 ID")
        private int brandId;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "브랜드 로고 이미지")
        private String brandLogo;
        
        @Schema(description = "브랜드 배너 이미지")
        private String brandBanner;
        
        @Schema(description = "브랜드 슬로건")
        private String slogan;
    }
    
    /**
     * 스크랩이 많은 상위 브랜드 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "스크랩이 많은 상위 브랜드 정보")
    public static class TopScrapedBrandInfo {
        
        @Schema(description = "브랜드 ID")
        private int brandId;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "브랜드 로고 이미지")
        private String brandLogo;
        
        @Schema(description = "브랜드 배너 이미지")
        private String brandBanner;
        
        @Schema(description = "브랜드 슬로건")
        private String slogan;
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
        private int id;
        
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
    
    /**
     * 정렬된 브랜드 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "정렬된 브랜드 정보")
    public static class SortedBrandInfo {
        
        @Schema(description = "브랜드 ID")
        private int brandId;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "브랜드 로고 이미지")
        private String brandLogo;
        
        @Schema(description = "브랜드 배너 이미지")
        private String brandBanner;
        
        @Schema(description = "브랜드 장르")
        private String genre;
        
        @Schema(description = "브랜드 슬로건")
        private String slogan;
    }
    
    /**
     * 브랜드 검색 결과 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "브랜드 검색 결과")
    public static class BrandSearchInfo {
        
        @Schema(description = "브랜드 ID")
        private int brandId;
        
        @Schema(description = "브랜드 이름")
        private String brandName;
        
        @Schema(description = "브랜드 로고 이미지")
        private String brandLogo;
        
        @Schema(description = "브랜드 배너 이미지")
        private String brandBanner;


        @Schema(description = "브랜드 슬로건")
        private String slogan;

    }
}
