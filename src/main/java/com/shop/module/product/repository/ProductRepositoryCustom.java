package com.shop.module.product.repository;

import com.shop.module.product.dto.ProductListSearchRequest;
import com.shop.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductRepositoryCustom {
    Page<Product> searchByKeywords(String query, Pageable pageable);

    Page<Product> findByCondition(ProductListSearchRequest productListSearchRequest, Pageable pageable);

    Page<Product> findAllExcludingRecommended(List<Long> excludedProductIds, Pageable pageable);

    Page<Product> findByConditionExcludingRecommended(ProductListSearchRequest productListSearchRequest, List<Long> excludedProductIds, Pageable pageable);
}