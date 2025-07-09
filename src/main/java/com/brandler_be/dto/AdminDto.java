package com.brandler_be.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AdminDto {

    public static class req {
        
        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "제품 이름 정규화 요청")
        public static class NormalizeProductName {
            
            @Schema(description = "제품 ID")
            private int productId;
            
            @Schema(description = "정규화할 제품 이름")
            private String productName;
        }
    }
    
    public static class res {
        
        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "정규화 결과")
        public static class NormalizeResult {
            
            @Schema(description = "제품 ID")
            private int productId;
            
            @Schema(description = "이전 제품 이름")
            private String oldProductName;
            
            @Schema(description = "정규화된 제품 이름")
            private String normalizedProductName;
            
            @Schema(description = "이전 제품 이름 HEX")
            private String oldProductNameHex;
            
            @Schema(description = "정규화된 제품 이름 HEX")
            private String normalizedProductNameHex;
            
            @Schema(description = "성공 여부")
            private boolean success;
        }
    }
}
