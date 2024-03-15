package com.shop.module.review.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.order.entity.Order;
import com.shop.module.user.entity.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "reviews")
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "order_id", referencedColumnName = "id")
    private Order order;

    @Column(nullable = false)
    private byte rating;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean activateFlag = true;

    @OneToMany(mappedBy = "review")
    @JsonBackReference
    private List<ReviewImage> reviewImages = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Review(User user, Order order, byte rating, String title, String content, Boolean activateFlag) {
        this.user = user;
        this.order = order;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.activateFlag = (activateFlag == null) ? true : activateFlag; // 기본값 설정
        // 관계형 필드는 생성자에서 초기화하지 않음
    }

    // 설정자 메서드들
    public void setUser(User user) {
        this.user = user;
    }

    public void setOrder(Order order) {
        this.order = order;
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

    public void setActivateFlag(boolean activateFlag) {
        this.activateFlag = activateFlag;
    }
}
