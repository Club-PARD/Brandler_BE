package com.brandler_be.repository;

import com.brandler_be.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    /**
     * 브랜드 ID로 상품 목록 조회
     * 
     * @param brandId 브랜드 ID
     * @return 상품 목록
     */
    List<Product> findByBrandId(Long brandId);
}
