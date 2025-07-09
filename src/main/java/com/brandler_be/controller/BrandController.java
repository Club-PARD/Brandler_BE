package com.brandler_be.controller;

import com.brandler_be.dto.BrandDto.res;
import com.brandler_be.service.BrandService;
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
@Tag(name = "브랜드 API", description = "브랜드 정보 관련 API")
public class BrandController {

    private final BrandService brandService;

    @Operation(summary = "브랜드 정보 조회", description = "브랜드 정보를 조회하고 방문 기록을 저장합니다.")
    @GetMapping("/brand/{email}/{brandId}")
    public ResponseEntity<res.BrandInfo> getBrandInfo(
            @PathVariable String email,
            @PathVariable int brandId) {
        return ResponseEntity.ok(brandService.getBrandInfoAndRecordVisit(email, brandId));
    }
    
    @Operation(summary = "최근 방문한 브랜드 목록", description = "사용자가 최근에 방문한 브랜드 목록을 조회합니다.")
    @GetMapping("/recent/{email}")
    public ResponseEntity<List<res.RecentVisitInfo>> getRecentVisitedBrands(
            @PathVariable String email) {
        return ResponseEntity.ok(brandService.getRecentVisitedBrands(email));
    }
    
    @Operation(summary = "스크랩이 많은 상위 10개 브랜드", description = "스크랩이 가장 많은 상위 10개 브랜드를 조회합니다.")
    @GetMapping("/top10")
    public ResponseEntity<List<res.TopScrapedBrandInfo>> getTopScrapedBrands() {
        return ResponseEntity.ok(brandService.getTopScrapedBrands());
    }
    
    @Operation(summary = "정렬된 브랜드 목록", description = "사용자가 스크랩하지 않은 브랜드를 먼저 보여주고, 스크랩한 브랜드는 상품 수가 적은 순으로 정렬하여 보여줍니다.")
    @GetMapping("/brands/sort/{email}")
    public ResponseEntity<List<res.SortedBrandInfo>> getSortedBrands(
            @PathVariable String email) {
        return ResponseEntity.ok(brandService.getSortedBrands(email));
    }
    

}
