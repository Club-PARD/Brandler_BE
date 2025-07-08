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
    public List<res.ProductInfo> getProductsByBrandId(Long brandId) {
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
                        .productName(product.getProductName())
                        .productImageName(product.getProductImage())
                        .productCategory(product.getCategory())
                        .price(product.getPrice())
                        .build())
                .toList();
    }
}
