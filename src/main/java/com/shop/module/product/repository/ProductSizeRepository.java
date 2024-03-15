package com.shop.module.product.repository;

import com.shop.module.product.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductSizeRepository extends JpaRepository<ProductSize, Long> {

    List<ProductSize> findAllByIdIn(List<Long> ids);

    List<ProductSize> findAllByProduct_Id(Long productId);

    @Modifying
    @Query("UPDATE ProductSize ps SET ps.quantity = ps.quantity + :quantity WHERE ps.id = :id")
    void restoreQuantity(@Param("id") Long id, @Param("quantity") int quantity);
}
