package com.brandler_be.service;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.User;
import com.brandler_be.domain.Visit;
import com.brandler_be.dto.BrandDto.res;
import com.brandler_be.repository.BrandRepository;
import com.brandler_be.repository.ScrapRepository;
import com.brandler_be.repository.UserRepository;
import com.brandler_be.repository.VisitRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrandService {

    private final BrandRepository brandRepository;
    private final UserRepository userRepository;
    private final VisitRepository visitRepository;
    private final ScrapRepository scrapRepository;

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
    
    /**
     * 사용자가 최근에 방문한 브랜드 목록 조회
     * 같은 브랜드를 중복 방문한 경우 가장 최근 방문 기록만 포함
     * 
     * @param email 사용자 이메일
     * @return 최근 방문한 브랜드 목록
     */
    @Transactional(readOnly = true)
    public List<res.RecentVisitInfo> getRecentVisitedBrands(String email) {
        log.info("사용자 최근 방문 브랜드 목록 조회: {}", email);
        
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        // 방문 기록 조회
        List<Visit> visits = visitRepository.findByUserOrderByVisitedAtDesc(user);
        
        // 중복 브랜드 제거 (가장 최근 방문 기록만 유지)
        Set<Long> processedBrandIds = new HashSet<>();
        List<res.RecentVisitInfo> result = new ArrayList<>();
        final int MAX_RESULTS = 20; // 최대 20개로 제한

        for (Visit visit : visits) {
            // 결과가 20개에 도달하면 루프 중단
            if (result.size() >= MAX_RESULTS) {
                break;
            }
            
            Brand brand = visit.getBrand();
            Long brandId = brand.getId();
            
            // 이미 처리한 브랜드 ID가 아닌 경우에만 추가
            if (processedBrandIds.add(brandId)) {  // Set.add()는 이미 존재하지 않을 때만 true 반환
                result.add(res.RecentVisitInfo.builder()
                        .brandId(brandId)
                        .brandName(brand.getBrandName())
                        .brandLogo(brand.getBrandLogo())
                        .brandBanner(brand.getBrandBanner())
                        .slogan(brand.getSlogan())
                        .build());
            }
        }
        
        log.info("사용자 {} 최근 방문 브랜드 {}개 조회 완료", email, result.size());
        return result;
    }
    
    /**
     * 스크랩이 가장 많은 상위 10개 브랜드 조회
     * 
     * @return 스크랩 수가 많은 상위 10개 브랜드 목록
     */
    @Transactional(readOnly = true)
    public List<res.TopScrapedBrandInfo> getTopScrapedBrands() {
        log.info("스크랩이 가장 많은 상위 10개 브랜드 조회");
        
        final int TOP_BRANDS_LIMIT = 10;
        
        // 스크랩이 많은 순으로 브랜드 조회
        List<Object[]> topBrandsWithCount = scrapRepository.findTopBrandsByScrapCount();
        
        List<res.TopScrapedBrandInfo> result = new ArrayList<>();
        
        for (Object[] brandWithCount : topBrandsWithCount) {
            if (result.size() >= TOP_BRANDS_LIMIT) {
                break;
            }
            
            Brand brand = (Brand) brandWithCount[0];
            
            result.add(res.TopScrapedBrandInfo.builder()
                    .brandId(brand.getId())
                    .brandName(brand.getBrandName())
                    .brandLogo(brand.getBrandLogo())
                    .brandBanner(brand.getBrandBanner())
                    .slogan(brand.getSlogan())
                    .build());
        }
        
        log.info("스크랩이 가장 많은 상위 {}개 브랜드 조회 완료", result.size());
        return result;
    }
    
    /**
     * 키워드로 브랜드 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색된 브랜드 목록
     */
    @Transactional(readOnly = true)
    public List<res.BrandSearchInfo> searchBrandsByKeyword(String keyword) {
        log.info("키워드로 브랜드 검색: {}", keyword);
        
        List<Brand> brands = brandRepository.findByBrandNameContainingIgnoreCase(keyword);
        
        List<res.BrandSearchInfo> result = brands.stream()
                .map(brand -> res.BrandSearchInfo.builder()
                        .brandId(brand.getId())
                        .brandName(brand.getBrandName())
                        .brandLogo(brand.getBrandLogo())
                        .brandBanner(brand.getBrandBanner())
                        .build())
                .collect(Collectors.toList());
        
        log.info("키워드 '{}' 검색 결과: {}개 브랜드 조회됨", keyword, result.size());
        return result;
    }
}
