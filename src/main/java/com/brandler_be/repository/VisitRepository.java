package com.brandler_be.repository;

import com.brandler_be.domain.User;
import com.brandler_be.domain.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Integer> {
    
    /**
     * 사용자가 최근에 방문한 브랜드 목록 조회
     * 같은 브랜드를 중복 방문한 경우 가장 최근 방문 기록만 포함
     * 
     * @param user 사용자
     * @return 방문 기록 목록
     */
    @Query("SELECT v FROM Visit v WHERE v.user = :user ORDER BY v.visitedAt DESC")
    List<Visit> findByUserOrderByVisitedAtDesc(@Param("user") User user);
}
