package com.bridgeshop.module.stats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StatsSalesResponse {
    private LocalDate startDate;
    private LocalDate endDate;
    private int soldOrderCount;
    private int canceledOrderCount;
    private int soldAmount;
    private int refundAmount;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public StatsSalesResponse(LocalDate startDate, LocalDate endDate,
                             int soldOrderCount, int canceledOrderCount,
                             int soldAmount, int refundAmount) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.soldOrderCount = soldOrderCount;
        this.canceledOrderCount = canceledOrderCount;
        this.soldAmount = soldAmount;
        this.refundAmount = refundAmount;
    }
}
