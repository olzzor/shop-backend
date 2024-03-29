package com.shop.module.order.dto;

import com.shop.module.review.dto.ReviewDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderReviewOverviewResponse {
    private List<ReviewDto> reviews;
    private int totalPages;

    private long totalReviews; // 전체 리뷰 수
    private double averageRating; // 전체 리뷰 평점
    private Map<Byte, Long> countRating; // 점수별 리뷰 수

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderReviewOverviewResponse(List<ReviewDto> reviews, int totalPages,
                                       long totalReviews, double averageRating, Map<Byte, Long> countRating) {
        this.reviews = reviews;
        this.totalPages = totalPages;
        this.totalReviews = totalReviews;
        this.averageRating = averageRating;
        this.countRating = countRating;
    }
}
