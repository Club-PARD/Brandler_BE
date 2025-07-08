package com.brandler_be.controller;

import com.brandler_be.dto.ProductDto.res;
import com.brandler_be.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "상품 API", description = "상품 정보 관련 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "브랜드별 상품 목록 조회", description = "특정 브랜드에 속한 모든 상품 목록을 조회합니다.")
    @GetMapping("/products/{brandId}")
    public ResponseEntity<List<res.ProductInfo>> getProductsByBrandId(
            @PathVariable Long brandId) {
        return ResponseEntity.ok(productService.getProductsByBrandId(brandId));
    }
    

}
