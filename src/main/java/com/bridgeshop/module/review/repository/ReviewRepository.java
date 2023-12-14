package com.bridgeshop.module.review.repository;

import com.bridgeshop.module.review.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Optional<Review> findByOrder_Id(Long orderId);

    Page<Review> findAllByActivateFlag(boolean activateFlag, Pageable pageable);
}