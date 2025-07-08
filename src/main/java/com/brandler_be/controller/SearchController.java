package com.brandler_be.controller;

import com.brandler_be.dto.BrandDto.res.BrandSearchInfo;
import com.brandler_be.dto.ProductDto.res.ProductSearchInfo;
import com.brandler_be.service.BrandService;
import com.brandler_be.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/search")
@Tag(name = "검색 API", description = "브랜드 및 상품 검색 관련 API")
public class SearchController {

    private final BrandService brandService;
    private final ProductService productService;

    @Operation(summary = "브랜드 검색", description = "키워드로 브랜드를 검색합니다.")
    @GetMapping("/brand/{keyword}")
    public ResponseEntity<List<BrandSearchInfo>> searchBrands(
            @PathVariable String keyword) {
        log.info("브랜드 검색 요청: keyword={}", keyword);
        return ResponseEntity.ok(brandService.searchBrandsByKeyword(keyword));
    }

    @Operation(summary = "상품 검색", description = "키워드로 상품을 검색합니다.")
    @GetMapping("/product/{keyword}")
    public ResponseEntity<List<ProductSearchInfo>> searchProducts(
            @PathVariable String keyword) {
        log.info("상품 검색 요청: keyword={}", keyword);
        return ResponseEntity.ok(productService.searchProductsByKeyword(keyword));
    }
}
