package com.bridgeshop.module.stats.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stats_sales")
public class StatsSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 통계의 대상 날짜
    @Column(nullable = false, unique = true)
    private LocalDate referenceDate = LocalDate.now().minusDays(1);

    // 해당 날짜의 총 판매 건수
    @Column(nullable = false)
    private int soldOrderCount = 0;

    // 해당 날짜의 총 취소 건수
    @Column(nullable = false)
    private int canceledOrderCount = 0;

    // 해당 날짜의 총 판매 금액
    @Column(nullable = false)
    private int soldAmount = 0;

    // 해당 날짜의 총 환불 금액
    @Column(nullable = false)
    private int refundAmount = 0;

    public void addSoldOrderCount(int count) {
        this.soldOrderCount += count;
    }

    public void addCanceledOrderCount(int count) {
        this.canceledOrderCount += count;
    }

    public void addSoldAmount(int amount) {
        this.soldAmount += amount;
    }

    public void addRefundAmount(int amount) {
        this.refundAmount += amount;
    }
}
