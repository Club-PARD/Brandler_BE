package com.brandler_be.repository;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.Scrap;
import com.brandler_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    
    /**
     * 사용자와 브랜드로 스크랩 조회
     * 
     * @param user 사용자
     * @param brand 브랜드
     * @return 스크랩 정보
     */
    Optional<Scrap> findByUserAndBrand(User user, Brand brand);
    
    /**
     * 사용자의 모든 스크랩 조회
     * 
     * @param user 사용자
     * @return 스크랩 목록
     */
    List<Scrap> findByUserAndIsScrapedTrue(User user);
    
    /**
     * 스크랩이 많은 순으로 브랜드 조회
     * isScraped가 true인 스크랩만 집계
     * 
     * @return 브랜드 목록
     */
    @Query("SELECT s.brand, COUNT(s) as scrapCount FROM Scrap s WHERE s.isScraped = true GROUP BY s.brand ORDER BY scrapCount DESC")
    List<Object[]> findTopBrandsByScrapCount();
    
    /**
     * 사용자가 스크랩한 브랜드 ID 목록 조회
     * 
     * @param userEmail 사용자 이메일
     * @return 스크랩한 브랜드 ID 목록
     */
    @Query("SELECT s.brand.id FROM Scrap s WHERE s.user.email = :userEmail AND s.isScraped = true")
    List<Integer> findScrapedBrandIdsByUserEmail(@Param("userEmail") String userEmail);
}
