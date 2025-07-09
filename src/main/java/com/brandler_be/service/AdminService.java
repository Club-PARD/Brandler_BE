package com.brandler_be.service;

import com.brandler_be.domain.Product;
import com.brandler_be.dto.AdminDto;
import com.brandler_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 표준 Java 라이브러리만 사용
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final ProductRepository productRepository;

    /**
     * 제품 이름을 정규화하여 업데이트합니다.
     * 여러 Unicode 정규화 형태(NFC, NFD, NFKC, NFKD)를 적용하고
     * 가장 적합한 형태로 저장합니다.
     *
     * @param request 정규화 요청 정보
     * @return 정규화 결과
     */
    @Transactional
    public AdminDto.res.NormalizeResult normalizeProductName(AdminDto.req.NormalizeProductName request) {
        log.info("제품 이름 정규화 요청: productId={}, productName={}", request.getProductId(), request.getProductName());
        
        // 제품 조회
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 제품입니다: " + request.getProductId()));
        
        // 기존 제품 이름 저장
        String oldProductName = product.getProductName();
        String oldProductNameHex = bytesToHex(oldProductName.getBytes(StandardCharsets.UTF_8));
        
        // 정규화 적용
        String normalizedName = Normalizer.normalize(request.getProductName(), Normalizer.Form.NFC);
        
        // 제품 이름 업데이트
        product.updateProductName(normalizedName);
        productRepository.save(product);
        
        // 정규화된 이름의 HEX 값 계산
        String normalizedNameHex = bytesToHex(normalizedName.getBytes(StandardCharsets.UTF_8));
        
        log.info("제품 이름 정규화 완료: productId={}, oldName={}, newName={}", 
                request.getProductId(), oldProductName, normalizedName);
        log.info("HEX 비교: oldHex={}, newHex={}", oldProductNameHex, normalizedNameHex);
        
        // 결과 반환
        return AdminDto.res.NormalizeResult.builder()
                .productId(product.getId())
                .oldProductName(oldProductName)
                .normalizedProductName(normalizedName)
                .oldProductNameHex(oldProductNameHex)
                .normalizedProductNameHex(normalizedNameHex)
                .success(true)
                .build();
    }
    
    /**
     * 바이트 배열을 HEX 문자열로 변환
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString().toUpperCase();
    }
}
