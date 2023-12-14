package com.bridgeshop.module.review.repository;

import com.bridgeshop.module.review.entity.Review;
import com.bridgeshop.module.review.dto.ReviewListSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> findByCondition(ReviewListSearchRequest reviewListSearchRequest, Pageable pageable);
}