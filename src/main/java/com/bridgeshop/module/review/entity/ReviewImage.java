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

    @Column(nullable = false, length = 512)
    private String fileUrl;

    @Column(nullable = false, length = 512)
    private String fileKey;

    @Column(nullable = false)
    private int displayOrder;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ReviewImage(Review review, String fileUrl, String fileKey, int displayOrder) {
        this.review = review;
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
        this.displayOrder = displayOrder;
    }

    // 설정자 메서드들
    public void setReview(Review review) {
        this.review = review;
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
