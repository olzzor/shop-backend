package com.bridgeshop.module.order.dto;

import com.bridgeshop.module.review.dto.ReviewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderReviewOverviewResponse {
    private List<ReviewDto> reviews;
    private int totalPages;

    private long totalReviews; // 전체 리뷰 수
    private double averageRating; // 전체 리뷰 평점
    private Map<Byte, Long> countRating; // 점수별 리뷰 수
}
