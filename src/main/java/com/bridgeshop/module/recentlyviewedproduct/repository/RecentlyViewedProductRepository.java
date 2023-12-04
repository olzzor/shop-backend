package com.bridgeshop.module.recentlyviewedproduct.repository;

import com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecentlyViewedProductRepository extends JpaRepository<RecentlyViewedProduct, Long> {
    Optional<RecentlyViewedProduct> findByUser_IdAndProduct_Id(Long userId, Long productId);

    Page<RecentlyViewedProduct> findAllByUser_IdOrderByViewedAtDesc(Long userId, Pageable pageable);

    List<RecentlyViewedProduct> findAllByUser_IdOrderByViewedAtDesc(Long userId);
}
