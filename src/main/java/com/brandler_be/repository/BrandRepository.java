package com.brandler_be.repository;

import com.brandler_be.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {
    // 브랜드명에 키워드가 포함된 브랜드 검색 (대소문자 구분 없이)
    List<Brand> findByBrandNameContainingIgnoreCase(String keyword);
}
