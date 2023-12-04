package com.bridgeshop.module.favorite.repository;

import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.bridgeshop.module.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    boolean existsByUser_IdAndProductSize_Id(Long userId, Long productSizeId);

    boolean existsByUser_IdAndProduct_IdAndProductSize_Id(Long userId, Long productId, Long productSizeId);

    boolean existsByUserAndProductAndProductSize(User user, Product product, ProductSize productSize);

    List<Favorite> findAllByUser_Id(Long userId);

    Favorite findByUser_IdAndProduct_Id(Long userId, Long productId);

    Optional<Favorite> findByUser_IdAndProduct_IdAndProductSize_Id(Long userId, Long productId, Long productSizeId);

    Optional<Favorite> findByUser_IdAndProductSize_Id(Long userId, Long productSizeId);

    Optional<Favorite> findByUserAndProductAndProductSize(User user, Product product, ProductSize productSize);

    @Modifying
    @Query("DELETE FROM Favorite f WHERE f.user.id = :userId")
    void deleteAllByUserId(@Param("userId") Long userId);
}
