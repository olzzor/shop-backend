package com.shop.module.review.dto;

import lombok.Getter;

@Getter
public class ReviewEditRequest {
    private Long reviewId;
    private byte rating;
    private String title;
    private String content;

    // 설정자 메서드들
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
