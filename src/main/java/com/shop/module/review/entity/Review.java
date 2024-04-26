package com.shop.module.review.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.order.entity.OrderDetail;
import com.shop.module.user.entity.User;
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
    @JoinColumn(nullable = false, name = "order_detail_id", referencedColumnName = "id")
    private OrderDetail orderDetail;

    @Column(nullable = false)
    private byte rating;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private boolean isActivate = true;

    @OneToMany(mappedBy = "review")
    @JsonBackReference
    private List<ReviewImage> reviewImages = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Review(User user, OrderDetail orderDetail, byte rating, String title, String content, Boolean isActivate) {
        this.user = user;
        this.orderDetail = orderDetail;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.isActivate = (isActivate == null) ? true : isActivate; // 기본값 설정
        // 관계형 필드는 생성자에서 초기화하지 않음
    }

    // 설정자 메서드들
    public void setUser(User user) {
        this.user = user;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
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

    public void setActivate(boolean isActivate) {
        this.isActivate = isActivate;
    }
}
