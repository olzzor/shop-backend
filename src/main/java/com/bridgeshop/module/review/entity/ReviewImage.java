package com.bridgeshop.module.review.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "review_images")
public class ReviewImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "review_id", referencedColumnName = "id")
    private Review review;

    @Column(nullable = false, length = 100)
    private String filePath;

    @Column(nullable = false, length = 100)
    private String fileName;

    @Column(nullable = false)
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ReviewImage(Review review, String filePath, String fileName, int displayOrder) {
        this.review = review;
        this.filePath = filePath;
        this.fileName = fileName;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setReview(Review review) {
        this.review = review;
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
