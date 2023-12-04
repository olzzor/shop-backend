package com.bridgeshop.module.product.repository;

import com.bridgeshop.module.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
//    public interface ProductFileRepository extends JpaRepository<ProductFile, ProductFileId> {

    @Modifying
    @Query("DELETE FROM ProductImage pf WHERE pf.product.id = :productId")
    void deleteAllByProduct_Id(@Param("productId") Long productId);
}
