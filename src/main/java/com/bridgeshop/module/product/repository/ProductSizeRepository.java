package com.bridgeshop.module.product.repository;

import com.bridgeshop.module.product.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    List<ProductSize> findAllByIdIn(List<Long> ids);

    List<ProductSize> findAllByProduct_Id(Long productId);
}
