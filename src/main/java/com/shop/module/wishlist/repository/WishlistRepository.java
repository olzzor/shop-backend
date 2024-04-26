package com.shop.module.wishlist.repository;

import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    boolean existsByUser_IdAndProductSize_Id(Long userId, Long productSizeId);

    boolean existsByUser_IdAndProduct_IdAndProductSize_Id(Long userId, Long productId, Long productSizeId);

    boolean existsByUserAndProductAndProductSize(User user, Product product, ProductSize productSize);

    List<Wishlist> findAllByUser_Id(Long userId);

    Wishlist findByUser_IdAndProduct_Id(Long userId, Long productId);

    Optional<Wishlist> findByUser_IdAndProduct_IdAndProductSize_Id(Long userId, Long productId, Long productSizeId);

    Optional<Wishlist> findByUser_IdAndProductSize_Id(Long userId, Long productSizeId);

    Optional<Wishlist> findByUserAndProductAndProductSize(User user, Product product, ProductSize productSize);

    @Modifying
    @Query("DELETE FROM Wishlist f WHERE f.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
