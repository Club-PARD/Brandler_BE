package com.brandler_be.controller;

import com.brandler_be.dto.ScrapDto.req;
import com.brandler_be.dto.ScrapDto.res;
import com.brandler_be.service.ScrapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/scrap")
@Tag(name = "스크랩 API", description = "스크랩 관련 API")
public class ScrapController {

    private final ScrapService scrapService;

    @Operation(summary = "스크랩 상태 변경", description = "브랜드 스크랩 상태를 변경합니다.")
    @PatchMapping("/{email}/{brandId}")
    public ResponseEntity<Void> updateScrapStatus(
            @PathVariable String email,
            @PathVariable int brandId,
            @RequestBody req.ScrapStatus request) {
        scrapService.updateScrapStatus(email, brandId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "스크랩 여부 확인", description = "사용자가 해당 브랜드를 스크랩했는지 확인합니다.")
    @GetMapping("/{email}/{brandId}")
    public ResponseEntity<res.ScrapStatus> checkScrapStatus(
            @PathVariable String email,
            @PathVariable int brandId) {
        return ResponseEntity.ok(scrapService.checkScrapStatus(email, brandId));
    }

    @Operation(summary = "사용자가 스크랩한 브랜드들", description = "사용자의 스크랩 목록을 조회합니다.")
    @GetMapping("/{email}")
    public ResponseEntity<List<res.ScrapInfo>> getScrapList(
            @PathVariable String email) {
        return ResponseEntity.ok(scrapService.getScrapList(email));
    }
}
