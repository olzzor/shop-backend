package com.bridgeshop.module.product.repository;

import com.bridgeshop.module.product.entity.ProductStatus;
import com.bridgeshop.module.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findAllByIdIn(List<Long> ids);

    Page<Product> findAllByProductImages_DisplayOrder(int displayOrder, Pageable pageable);

    Page<Product> findAllByStatusAndProductImages_DisplayOrder(ProductStatus status, int displayOrder, Pageable pageable);

    Page<Product> findAllByCategory_CodeAndStatusAndProductImages_DisplayOrder(String categoryCode, ProductStatus status, int displayOrder, Pageable pageable);

}