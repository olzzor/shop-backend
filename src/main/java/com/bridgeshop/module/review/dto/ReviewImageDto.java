package com.bridgeshop.module.review.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ReviewImageDto {
    private Long id;
    private ReviewDto review;
    private String filePath;
    private String fileName;
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ReviewImageDto(Long id, String filePath, String fileName, int displayOrder) {
        this.id = id;
        this.filePath = filePath;
        this.fileName = fileName;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setReview(ReviewDto reviewDto) {
        this.review = reviewDto;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }
}
