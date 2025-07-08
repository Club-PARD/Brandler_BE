package com.brandler_be.repository;

import com.brandler_be.domain.Brand;
import com.brandler_be.domain.Scrap;
import com.brandler_be.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScrapRepository extends JpaRepository<Scrap, Long> {
    
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
}
