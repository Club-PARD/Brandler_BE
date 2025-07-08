package com.brandler_be.dto.ProductDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class res {

    /**
     * 상품 정보 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 정보 응답")
    public static class ProductInfo {
        
        @Schema(description = "상품명")
        private String productName;
        
        @Schema(description = "상품 이미지명")
        private String productImageName;
        
        @Schema(description = "상품 카테고리")
        private String productCategory;
        
        @Schema(description = "상품 가격")
        private int price;
    }
    
    /**
     * 상품 검색 결과 응답 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "상품 검색 결과")
    public static class ProductSearchInfo {
        
        @Schema(description = "브랜드 ID")
        private Long brandId;
        
        @Schema(description = "상품명")
        private String productName;
        
        @Schema(description = "상품 이미지")
        private String productImage;
    }
}
