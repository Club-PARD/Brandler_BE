package com.brandler_be.controller;

import com.brandler_be.dto.UserDto.res;
import com.brandler_be.service.BrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "브랜드 API", description = "브랜드 정보 관련 API")
public class BrandController {

    private final BrandService brandService;

    @Operation(summary = "브랜드 정보 조회", description = "브랜드 정보를 조회하고 방문 기록을 저장합니다.")
    @GetMapping("/brand/{email}/{brandId}")
    public ResponseEntity<res.BrandInfo> getBrandInfo(
            @PathVariable String email,
            @PathVariable Long brandId) {
        return ResponseEntity.ok(brandService.getBrandInfoAndRecordVisit(email, brandId));
    }
}
