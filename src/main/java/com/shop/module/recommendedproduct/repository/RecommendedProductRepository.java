package com.shop.module.recommendedproduct.repository;

import com.shop.module.recommendedproduct.entity.RecommendedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RecommendedProductRepository extends JpaRepository<RecommendedProduct, Long> {

    int countAllBy();

    @Query("SELECT rp.product.id FROM RecommendedProduct rp")
    List<Long> findAllProductIds();

    @Query("SELECT rp.displayOrder FROM RecommendedProduct rp WHERE rp.id = :id")
    Optional<Integer> findDisplayOrderById(Long id);

    List<RecommendedProduct> findAllByOrderByDisplayOrder();

    Page<RecommendedProduct> findAllByOrderByDisplayOrder(Pageable pageable);

    List<RecommendedProduct> findByDisplayOrderGreaterThan(int displayOrder);
}
