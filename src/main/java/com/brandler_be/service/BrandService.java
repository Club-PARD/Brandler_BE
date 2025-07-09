package com.brandler_be.service;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.User;
import com.brandler_be.domain.Visit;
import com.brandler_be.dto.BrandDto.res;
import com.brandler_be.repository.BrandRepository;
import com.brandler_be.repository.ScrapRepository;
import com.brandler_be.repository.UserRepository;
import com.brandler_be.repository.VisitRepository;
import com.brandler_be.repository.ProductRepository; // 추가
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
    private final ProductRepository productRepository;

    /**
     * 브랜드 정보 조회 및 방문 기록 저장
     * 
     * @param email 사용자 이메일
     * @param brandId 브랜드 ID
     * @return 브랜드 정보
     */
    @Transactional
    public res.BrandInfo getBrandInfoAndRecordVisit(String email, int brandId) {
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
        Set<Integer> processedBrandIds = new HashSet<>();
        List<res.RecentVisitInfo> result = new ArrayList<>();
        final int MAX_RESULTS = 20; // 최대 20개로 제한

        for (Visit visit : visits) {
            // 결과가 20개에 도달하면 루프 중단
            if (result.size() >= MAX_RESULTS) {
                break;
            }
            
            Brand brand = visit.getBrand();
            int brandId = brand.getId();
            
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
    /**
     * 정렬된 브랜드 목록 조회
     * 1. 사용자가 스크랩하지 않은 브랜드를 먼저 보여줌
     * 2. 스크랩한 브랜드 중에서 상품수가 적은 순으로 정렬
     * 
     * @param email 사용자 이메일
     * @return 정렬된 브랜드 목록
     */
    public List<res.SortedBrandInfo> getSortedBrands(String email) {
        log.info("정렬된 브랜드 목록 조회: {}", email);
        
        // 1. 사용자가 스크랩한 브랜드 ID 목록 조회
        List<Integer> scrapedBrandIds = scrapRepository.findScrapedBrandIdsByUserEmail(email);
        log.info("스크랩한 브랜드 수: {}", scrapedBrandIds.size());
        
        // 2. 모든 브랜드 조회
        List<Brand> allBrands = brandRepository.findAll();
        log.info("전체 브랜드 수: {}", allBrands.size());
        
        // 3. 브랜드별 상품 수 조회
        Map<Integer, Integer> brandProductCounts = new HashMap<>();
        productRepository.countProductsByBrand().forEach(result -> {
            int brandId = (int) result[0];
            int count = (int) result[1];
            brandProductCounts.put(brandId, count);
        });
        
        // 4. 스크랩하지 않은 브랜드와 스크랩한 브랜드 분리
        List<Brand> nonScrapedBrands = new ArrayList<>();
        List<Brand> scrapedBrands = new ArrayList<>();
        
        for (Brand brand : allBrands) {
            if (scrapedBrandIds.contains(brand.getId())) {
                scrapedBrands.add(brand);
            } else {
                nonScrapedBrands.add(brand);
            }
        }
        
        // 5. 스크랩한 브랜드는 상품 수가 적은 순으로 정렬
        scrapedBrands.sort((b1, b2) -> {
            Integer count1 = brandProductCounts.getOrDefault(b1.getId(), 0);
            Integer count2 = brandProductCounts.getOrDefault(b2.getId(), 0);
            return count1.compareTo(count2); // 오름차순 정렬 (적은 순)
        });
        
        // 6. 결과 병합 (스크랩하지 않은 브랜드 먼저, 그 다음 스크랩한 브랜드)
        List<Brand> sortedBrands = new ArrayList<>();
        sortedBrands.addAll(nonScrapedBrands);
        sortedBrands.addAll(scrapedBrands);
        
        // 7. DTO 변환
        return sortedBrands.stream()
                .map(brand -> res.SortedBrandInfo.builder()
                        .brandId(brand.getId())
                        .brandName(brand.getBrandName())
                        .brandLogo(brand.getBrandLogo())
                        .brandBanner(brand.getBrandBanner())
                        .genre(brand.getGenre())
                        .slogan(brand.getSlogan())
                        .build())
                .collect(Collectors.toList());
    }
    
    public List<res.BrandSearchInfo> searchBrandsByKeyword(String keyword) {
        log.info("키워드로 브랜드 검색: {}", keyword);
        
        List<Brand> brands = brandRepository.findByBrandNameContainingIgnoreCase(keyword);
        
        List<res.BrandSearchInfo> result = brands.stream()
                .map(brand -> res.BrandSearchInfo.builder()
                        .brandId(brand.getId())
                        .brandName(brand.getBrandName())
                        .brandLogo(brand.getBrandLogo())
                        .brandBanner(brand.getBrandBanner())
                        .slogan(brand.getSlogan())
                        .build())
                .collect(Collectors.toList());
        
        log.info("키워드 '{}' 검색 결과: {}개 브랜드 조회됨", keyword, result.size());
        return result;
    }
}
