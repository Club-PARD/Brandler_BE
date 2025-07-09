package com.brandler_be.repository;

import com.brandler_be.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    
    /**
     * 브랜드 ID로 상품 목록 조회
     * 
     * @param brandId 브랜드 ID
     * @return 상품 목록
     */
    List<Product> findByBrandId(int brandId);
    
    /**
     * 상품명에 키워드가 포함된 상품 검색 (대소문자 구분 없이)
     * 
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    List<Product> findByProductNameContainingIgnoreCase(String keyword);
    
    /**
     * 상품명에 키워드가 포함된 상품을 직접 쿼리로 검색 (JPQL 사용)
     * 
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Product> searchByProductNameCustom(@Param("keyword") String keyword);
    
    /**
     * 네이티브 SQL을 사용하여 상품명에서 키워드 검색
     * 인코딩 변환을 통해 한글 검색 문제 해결 시도
     * 
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    @Query(value = "SELECT * FROM product WHERE CONVERT(CAST(product_name AS BINARY) USING utf8mb4) LIKE CONCAT('%', :keyword, '%')", nativeQuery = true)
    List<Product> searchByProductNameNative(@Param("keyword") String keyword);
    
    /**
     * 정규식을 사용하여 상품명에서 키워드 검색
     * 
     * @param keyword 검색 키워드
     * @return 검색된 상품 목록
     */
    @Query(value = "SELECT * FROM product WHERE product_name REGEXP :keyword", nativeQuery = true)
    List<Product> searchByProductNameRegexp(@Param("keyword") String keyword);
    
    /**
     * 브랜드별 상품 수 조회
     * 
     * @return 브랜드 ID와 상품 수를 포함한 결과
     */
    @Query("SELECT p.brand.id, COUNT(p) FROM Product p GROUP BY p.brand.id")
    List<Object[]> countProductsByBrand();
    
    /**
     * 특정 브랜드의 상품 수 조회
     * 
     * @param brandId 브랜드 ID
     * @return 상품 수
     */
    @Query("SELECT COUNT(p) FROM Product p WHERE p.brand.id = :brandId")
    int countProductsByBrandId(@Param("brandId") int brandId);
}
