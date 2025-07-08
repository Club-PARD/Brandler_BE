package com.brandler_be.service;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.User;
import com.brandler_be.domain.Visit;
import com.brandler_be.dto.UserDto.res;
import com.brandler_be.repository.BrandRepository;
import com.brandler_be.repository.UserRepository;
import com.brandler_be.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;

    /**
     * 브랜드 정보 조회 및 방문 기록 저장
     * 
     * @param email 사용자 이메일
     * @param brandId 브랜드 ID
     * @return 브랜드 정보
     */
    @Transactional
    public res.BrandInfo getBrandInfoAndRecordVisit(String email, Long brandId) {
        log.info("브랜드 정보 조회 및 방문 기록 저장: 사용자={}, 브랜드ID={}", email, brandId);
        
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        // 브랜드 조회
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 브랜드입니다: " + brandId));
        
        // 방문 기록 저장
        Visit visit = Visit.builder()
                .user(user)
                .brand(brand)
                .build();
        visitRepository.save(visit);
        
        log.info("방문 기록 저장 완료: 사용자={}, 브랜드={}", email, brand.getBrandName());
        
        // 브랜드 정보 반환
        return res.BrandInfo.builder()
                .id(brand.getId())
                .brandName(brand.getBrandName())
                .brandLogo(brand.getBrandLogo())
                .brandBanner(brand.getBrandBanner())
                .description(brand.getDescription())
                .brandPageUrl(brand.getBrandPageUrl())
                .genre(brand.getGenre())
                .build();
    }
}
