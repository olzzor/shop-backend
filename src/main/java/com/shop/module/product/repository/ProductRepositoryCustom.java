package com.shop.module.product.repository;

import com.shop.module.product.dto.ProductListSearchRequest;
import com.shop.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchByKeywords(String query, Pageable pageable);

    Page<Product> findByCondition(ProductListSearchRequest productListSearchRequest, Pageable pageable);
}