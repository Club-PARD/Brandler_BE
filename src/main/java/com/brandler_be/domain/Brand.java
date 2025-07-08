package com.brandler_be.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 브랜드 엔티티
 */
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "브랜드 정보")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    private List<Visit> visits = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "brand")
    private List<Product> products = new ArrayList<>();
}
