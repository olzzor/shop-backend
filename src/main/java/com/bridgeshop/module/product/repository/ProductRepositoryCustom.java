package com.bridgeshop.module.product.repository;

import com.bridgeshop.module.product.dto.ProductListSearchRequest;
import com.bridgeshop.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepositoryCustom {
    Page<Product> searchByKeywords(String query, Pageable pageable);

    Page<Product> findByCondition(ProductListSearchRequest productListSearchRequest, Pageable pageable);
}