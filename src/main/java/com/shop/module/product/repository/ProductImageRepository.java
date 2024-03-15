package com.shop.module.product.repository;

import com.shop.module.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
    //    public interface ProductFileRepository extends JpaRepository<ProductFile, ProductFileId> {
    List<ProductImage> findAllByProduct_Id(Long productId);

    @Modifying
    @Query("DELETE FROM ProductImage pf WHERE pf.product.id = :productId")
    void deleteAllByProduct_Id(@Param("productId") Long productId);
}
