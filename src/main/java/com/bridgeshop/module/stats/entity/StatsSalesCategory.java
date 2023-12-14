package com.bridgeshop.module.stats.entity;

import com.bridgeshop.module.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Getter
@Table(name = "stats_sales_category")
public class StatsSalesCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private LocalDate referenceDate; // 통계의 대상 날짜

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "category_id", referencedColumnName = "id")
    private Category category; // 통계의 대상 카테고리

    @Column(nullable = false)
    private int soldOrderCount; // 해당 카테고리의 총 판매 건수

    @Column(nullable = false)
    private int canceledOrderCount; // 해당 카테고리의 총 취소 건수

    @Column(nullable = false)
    private int soldAmount; // 해당 카테고리의 총 판매 금액

    @Column(nullable = false)
    private int refundAmount; // 해당 카테고리의 총 환불 금액

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public StatsSalesCategory(LocalDate referenceDate, Category category,
                              int soldOrderCount, int canceledOrderCount,
                              int soldAmount, int refundAmount) {
        this.referenceDate = referenceDate;
        this.category = category;
        this.soldOrderCount = soldOrderCount;
        this.canceledOrderCount = canceledOrderCount;
        this.soldAmount = soldAmount;
        this.refundAmount = refundAmount;
    }

    // 설정자 메서드들
    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setSoldOrderCount(int soldOrderCount) {
        this.soldOrderCount = soldOrderCount;
    }

    public void setCanceledOrderCount(int canceledOrderCount) {
        this.canceledOrderCount = canceledOrderCount;
    }

    public void setSoldAmount(int soldAmount) {
        this.soldAmount = soldAmount;
    }

    public void setRefundAmount(int refundAmount) {
        this.refundAmount = refundAmount;
    }
}
