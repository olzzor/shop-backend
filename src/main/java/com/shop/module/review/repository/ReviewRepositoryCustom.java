package com.shop.module.review.repository;

import com.shop.module.review.entity.Review;
import com.shop.module.review.dto.ReviewListSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewRepositoryCustom {
    Page<Review> findByCondition(ReviewListSearchRequest reviewListSearchRequest, Pageable pageable);
}