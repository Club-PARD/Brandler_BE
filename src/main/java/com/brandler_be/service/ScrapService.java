package com.brandler_be.service;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.Scrap;
import com.brandler_be.domain.User;
import com.brandler_be.dto.ScrapDto.req;
import com.brandler_be.dto.ScrapDto.res;
import com.brandler_be.repository.BrandRepository;
import com.brandler_be.repository.ScrapRepository;
import com.brandler_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final BrandRepository brandRepository;

    /**
     * 스크랩 상태 변경
     * 
     * @param email 사용자 이메일
     * @param brandId 브랜드 ID
     * @param request 스크랩 상태 변경 요청
     */
    @Transactional
    public void updateScrapStatus(String email, int brandId, req.ScrapStatus request) {
        log.info("스크랩 상태 변경: 사용자={}, 브랜드ID={}, 스크랩여부={}", email, brandId, request.getIsScraped());
        
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        // 브랜드 조회
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 브랜드입니다: " + brandId));
        
        // 스크랩 정보 조회 또는 생성
        Scrap scrap = scrapRepository.findByUserAndBrand(user, brand)
                .orElseGet(() -> Scrap.builder()
                        .user(user)
                        .brand(brand)
                        .isScraped(false)
                        .build());
        
        // 스크랩 상태 변경
        scrap.updateScrapStatus(request.getIsScraped());
        scrapRepository.save(scrap);
        
        log.info("스크랩 상태 변경 완료: 사용자={}, 브랜드={}, 스크랩여부={}", 
                email, brand.getBrandName(), request.getIsScraped());
    }
    
    /**
     * 스크랩 여부 확인
     * 
     * @param email 사용자 이메일
     * @param brandId 브랜드 ID
     * @return 스크랩 여부
     */
    @Transactional(readOnly = true)
    public res.ScrapStatus checkScrapStatus(String email, int brandId) {
        log.info("스크랩 여부 확인: 사용자={}, 브랜드ID={}", email, brandId);
        
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        // 브랜드 조회
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 브랜드입니다: " + brandId));
        
        // 스크랩 정보 조회
        boolean isScraped = scrapRepository.findByUserAndBrand(user, brand)
                .map(Scrap::getIsScraped)
                .orElse(false);
        
        log.info("스크랩 여부 확인 완료: 사용자={}, 브랜드={}, 스크랩여부={}", 
                email, brand.getBrandName(), isScraped);
        
        return res.ScrapStatus.builder()
                .isScraped(isScraped)
                .build();
    }
    
    /**
     * 사용자의 스크랩 목록 조회
     * 
     * @param email 사용자 이메일
     * @return 스크랩 목록
     */
    @Transactional(readOnly = true)
    public List<res.ScrapInfo> getScrapList(String email) {
        log.info("사용자 스크랩 목록 조회: {}", email);
        
        // 사용자 조회
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 사용자입니다: " + email));
        
        // 스크랩 목록 조회
        List<Scrap> scraps = scrapRepository.findByUserAndIsScrapedTrue(user);
        log.info("사용자 {} 스크랩 {}개 조회 완료", email, scraps.size());
        
        // 스크랩 정보 DTO로 변환하여 반환
        return scraps.stream()
                .map(scrap -> {
                    Brand brand = scrap.getBrand();
                    return res.ScrapInfo.builder()
                        .brandId(brand.getId())
                        .brandName(brand.getBrandName())
                        .brandLogo(brand.getBrandLogo())
                        .brandBanner(brand.getBrandBanner())
                        .slogan(brand.getSlogan())
                        .build();
                })
                .toList();
    }
    
    /**
     * [개발용] 특정 브랜드의 스크랩 수 설정
     * 테스트 목적으로만 사용해야 함
     * 
     * @param brandId 브랜드 ID
     * @param count 설정할 스크랩 수
     * @return 테스트 결과 정보
     */
    @Transactional
    public res.TestScrapResult setScrapCountForTesting(int brandId, int count) {
        log.info("[개발용] 브랜드 ID {} 스크랩 수 {} 설정", brandId, count);
        
        // 브랜드 확인
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 브랜드입니다: " + brandId));
        
        // 기존 테스트 스크랩 삭제 (test로 시작하는 이메일 사용자의 스크랩만 삭제)
        List<Scrap> existingTestScraps = scrapRepository.findByBrandIdAndUserEmailStartingWith(brandId, "test");
        scrapRepository.deleteAll(existingTestScraps);
        
        AtomicInteger createdUsers = new AtomicInteger(0);
        
        // 테스트용 사용자 생성 및 스크랩 추가
        for (int i = 0; i < count; i++) {
            final int index = i; // 람다에서 사용할 불변 변수
            String email = "test" + index + "@example.com";
            
            // 사용자가 없으면 생성
            User user = userRepository.findByEmail(email)
                    .orElseGet(() -> {
                        User newUser = User.builder()
                                .email(email)
                                .name("TestUser" + index)
                                .genre("캐주얼") // 기본 장르 설정
                                .build();
                        createdUsers.incrementAndGet();
                        return userRepository.save(newUser);
                    });
            
            // 스크랩 생성
            Scrap scrap = Scrap.builder()
                    .user(user)
                    .brand(brand)
                    .isScraped(true)
                    .scrapedAt(LocalDateTime.now())
                    .build();
            
            scrapRepository.save(scrap);
        }
        
        log.info("[개발용] 브랜드 {} 스크랩 수 {} 설정 완료, 생성된 사용자: {}", 
                brand.getBrandName(), count, createdUsers.get());
        
        return res.TestScrapResult.builder()
                .brandId(brandId)
                .brandName(brand.getBrandName())
                .scrapCount(count)
                .createdUsers(createdUsers.get())
                .build();
    }
}
