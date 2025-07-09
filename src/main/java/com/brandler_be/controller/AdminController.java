package com.brandler_be.controller;

import com.brandler_be.dto.AdminDto;
import com.brandler_be.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@Tag(name = "한글 API", description = "for 한글검색 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "제품 이름 정규화", description = "제품 이름을 정규화된 형태로 업데이트합니다.")
    @PostMapping("/product/normalize")
    public ResponseEntity<AdminDto.res.NormalizeResult> normalizeProductName(
            @RequestBody AdminDto.req.NormalizeProductName request) {
        return ResponseEntity.ok(adminService.normalizeProductName(request));
    }
}
