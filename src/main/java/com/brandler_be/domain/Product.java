package com.brandler_be.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;


/*
상품 엔티티
*/
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "상품 정보")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "상품 ID", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "상품명")
    private String productName;

    @Schema(description = "상품 이미지 ")
    private String productImage;

    @Schema(description = "상품 가격")
    private int price;

    @Column(name = "category", length = 50)
    @Schema(description = "상품 카테고리", example = "티셔츠")
    private String category;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    @Schema(description = "상품이 속한 브랜드")
    private Brand brand;

}
