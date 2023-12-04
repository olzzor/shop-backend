package com.bridgeshop.module.order.repository;

import com.bridgeshop.module.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long>, OrderRepositoryCustom {

    Optional<Order> findByOrderNumber(String orderNumber);

    Page<Order> findAllByOrderByOrderNumberDesc(Pageable pageable);

    Page<Order> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    @Query("SELECT id FROM Order WHERE orderNumber = :orderNumber")
    Long findIdByOrderNumber(String orderNumber);
}
