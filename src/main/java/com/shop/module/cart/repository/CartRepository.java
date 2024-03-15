package com.shop.module.cart.repository;

import com.shop.module.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUser_Id(Long userId);

    @Query("SELECT c.id FROM Cart c WHERE c.user.id = :userId")
    Optional<Long> findIdByUser_Id(Long userId);
}
