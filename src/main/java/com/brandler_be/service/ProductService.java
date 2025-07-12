package com.brandler_be.service;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.Product;
import com.brandler_be.dto.ProductDto.res;
import com.brandler_be.repository.BrandRepository;
import com.brandler_be.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;

    /**
     * 브랜드 ID로 상품 목록 조회
     * 
     * @param brandId 브랜드 ID
     * @return 상품 정보 목록
     */
    @Transactional(readOnly = true)
    public List<res.ProductInfo> getProductsByBrandId(Integer brandId) {
        log.info("브랜드 ID로 상품 목록 조회: brandId={}", brandId);
        
        // 브랜드 존재 여부 확인
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 브랜드입니다: " + brandId));
        
        // 브랜드에 속한 상품 목록 조회
        List<Product> products = productRepository.findByBrandId(brandId);
        log.info("브랜드 {} 상품 {}개 조회 완료", brand.getBrandName(), products.size());
        
        // 상품 정보 DTO로 변환하여 반환
        return products.stream()
                .map(product -> res.ProductInfo.builder()
                        .productId(product.getId())
                        .productName(product.getProductName())
                        .productImageName(product.getProductImage())
                        .productCategory(product.getCategory())
                        .price(product.getPrice())
                        .build())
                .toList();
    }
    
    /**
     * 키워드로 상품 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    @Transactional(readOnly = true)
    public List<res.ProductSearchInfo> searchProductsByKeyword(String keyword) {
        log.info("키워드로 상품 검색: {}", keyword);
        try {
            // 키워드 전처리 - 앞뒤 공백 제거 및 유니코드 정규화
            String originalKeyword = keyword.trim();
            
            // 유니코드 정규화 적용 (NFC 형식으로 변환)
            String normalizedKeyword = Normalizer.normalize(originalKeyword, Normalizer.Form.NFC);
            log.info("전처리 후 검색 키워드: '{}', 정규화 후: '{}'", originalKeyword, normalizedKeyword);
            
            // 정규화 전후 바이트 값 비교를 위한 로깅
            log.info("원본 키워드 바이트: {}", bytesToHex(originalKeyword.getBytes("UTF-8")));
            log.info("정규화 키워드 바이트: {}", bytesToHex(normalizedKeyword.getBytes("UTF-8")));
            
            // 정규화된 키워드 사용
            final String searchKeyword = normalizedKeyword;
            
            // 여러 검색 방법 시도
            List<Product> products = new ArrayList<>();
            
            // 1. 네이티브 SQL로 검색 시도
            products = productRepository.searchByProductNameNative(searchKeyword);
            log.info("네이티브 SQL로 검색된 상품 수: {}", products.size());
            
            // 결과가 없으면 정규식으로 검색 시도
            if (products.isEmpty()) {
                log.info("정규식으로 검색 시도");
                products = productRepository.searchByProductNameRegexp(searchKeyword);
                log.info("정규식으로 검색된 상품 수: {}", products.size());
            }
            
            // 여전히 결과가 없으면 모든 상품을 가져와서 Java에서 필터링
            if (products.isEmpty()) {
                log.info("Java에서 필터링 시도");
                List<Product> allProducts = productRepository.findAll();
                products = allProducts.stream()
                    .filter(product -> {
                        if (product.getProductName() == null) return false;
                        return product.getProductName().toLowerCase().contains(searchKeyword.toLowerCase());
                    })
                    .collect(Collectors.toList());
                log.info("Java 필터링으로 검색된 상품 수: {}", products.size());
                
                // 디버깅을 위해 모든 상품명 출력
                if (products.isEmpty()) {
                    log.info("모든 상품명 목록:");
                    allProducts.forEach(product -> 
                        log.info("상품ID: {}, 상품명: '{}'", product.getId(), product.getProductName()));
                }
            }
            
            // 디버깅을 위해 검색된 상품명 로깅
            if (products.isEmpty()) {
                log.info("검색 결과가 없습니다.");
            } else {
                log.info("검색된 상품명 목록:");
                products.forEach(product ->
                    log.info("상품ID: {}, 상품명: '{}'", product.getId(), product.getProductName()));
            }
            
            List<res.ProductSearchInfo> result = getProductSearchInfoList(products);
            
            log.info("키워드 '{}' 검색 결과: {}개 상품 조회됨", searchKeyword, result.size());
            return result;
        } catch (Exception e) {
            log.error("상품 검색 중 오류 발생: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }
    
    /**
     * 바이트 배열을 16진수 문자열로 변환
     * 
     * @param bytes 변환할 바이트 배열
     * @return 16진수 문자열
     */
    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }
    
    /**
     * 상품 목록을 ProductSearchInfo DTO 목록으로 변환
     * 
     * @param products 상품 목록
     * @return ProductSearchInfo DTO 목록
     */
    private List<res.ProductSearchInfo> getProductSearchInfoList(List<Product> products) {
        return products.stream()
                .map(product -> res.ProductSearchInfo.builder()
                        .brandId(product.getBrand() != null ? product.getBrand().getId() : null)
                        .productName(product.getProductName())
                        .productImage(product.getProductImage())
                        .price(product.getPrice())
                        .build())
                .collect(Collectors.toList());
    }
}
