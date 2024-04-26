package com.shop.module.product.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "log_product_view")
public class LogProductView {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column
    private Long userId;

    @Column(nullable = false)
    private LocalDateTime viewedAt;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime regDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public LogProductView(Long productId, Long userId, LocalDateTime viewedAt, LocalDateTime regDate) {
        this.productId = productId;
        this.userId = userId;
        this.viewedAt = viewedAt;
    }

    // 설정자 메서드들
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setViewedAt(LocalDateTime viewedAt) {
        this.viewedAt = viewedAt;
    }
}
