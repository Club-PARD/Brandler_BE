package com.brandler_be.dto.ScrapDto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class req {

    /**
     * 스크랩 상태 변경 요청 DTO
     */
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "스크랩 상태 변경 요청")
    public static class ScrapStatus {
        
        @Schema(description = "스크랩 여부", example = "true")
        private Boolean isScraped;
    }
}
