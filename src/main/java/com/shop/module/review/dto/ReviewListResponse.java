package com.shop.module.review.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewListResponse {
    private List<ReviewDto> reviews;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ReviewListResponse(List<ReviewDto> reviews, int totalPages) {
        this.reviews = reviews;
        this.totalPages = totalPages;
    }

    // 설정자 메서드들
    public void setReviews(List<ReviewDto> reviewDtoList) {
        this.reviews = reviewDtoList;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}