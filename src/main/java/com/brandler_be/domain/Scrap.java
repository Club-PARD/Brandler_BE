package com.brandler_be.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 스크랩 엔티티
 * 사용자가 브랜드를 스크랩한 정보를 관리합니다.
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "스크랩 정보")
public class Scrap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "스크랩 ID")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_email", nullable = false)
    @Schema(description = "스크랩한 사용자")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @Schema(description = "스크랩된 브랜드")
    private Brand brand;

    @Schema(description = "즐겨찾기 여부", example = "true")
    private Boolean isScraped;
    
    @Column(name = "scraped_at")
    @Schema(description = "스크랩 시간")
    private LocalDateTime scrapedAt;
    
    /**
     * 스크랩 상태 변경
     * 
     * @param isScraped 스크랩 여부
     */
    public void updateScrapStatus(Boolean isScraped) {
        this.isScraped = isScraped;
        if (isScraped) {
            this.scrapedAt = LocalDateTime.now();
        }
    }
}
