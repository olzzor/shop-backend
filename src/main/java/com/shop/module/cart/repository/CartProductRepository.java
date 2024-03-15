package com.shop.module.cart.repository;

import com.shop.module.cart.entity.Cart;
import com.shop.module.cart.entity.CartProduct;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartProductRepository extends JpaRepository<CartProduct, Long> {
    Optional<CartProduct> findByCartAndProductAndProductSize(Cart cart, Product product, ProductSize productSize);

    Optional<CartProduct> findByCart_IdAndProduct_Id(Long cartId, Long productId);

    List<CartProduct> findAllByCart_Id(Long cartId);

    void deleteByCart_Id(Long cartId);

    @Query("SELECT cp.id FROM CartProduct cp WHERE cp.cart.id = :cartId AND cp.product.id = :productId")
    Long findIdByCartAndProduct(@Param("cartId") Long cartId, @Param("productId") Long productId);
}