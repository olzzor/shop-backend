package com.shop.module.review.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewImageDto {
    private Long id;
    private ReviewDto review;
    private String fileUrl;
    private String fileKey;
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ReviewImageDto(Long id, String fileUrl, String fileKey, int displayOrder) {
        this.id = id;
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setReview(ReviewDto reviewDto) {
        this.review = reviewDto;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
