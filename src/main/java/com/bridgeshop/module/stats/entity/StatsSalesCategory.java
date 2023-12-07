package com.bridgeshop.module.stats.entity;

import com.bridgeshop.module.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stats_sales_category")
public class StatsSalesCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 통계의 대상 날짜
    @Column(nullable = false, unique = true)
    private LocalDate referenceDate = LocalDate.now().minusDays(1);

    // 통계의 대상 카테고리
    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "category_id", referencedColumnName = "id")
    private Category category;

    // 해당 카테고리의 총 판매 건수
    @Column(nullable = false)
    private int soldOrderCount = 0;

    // 해당 카테고리의 총 취소 건수
    @Column(nullable = false)
    private int canceledOrderCount = 0;

    // 해당 카테고리의 총 판매 금액
    @Column(nullable = false)
    private int soldAmount = 0;

    // 해당 카테고리의 총 환불 금액
    @Column(nullable = false)
    private int refundAmount = 0;
}
