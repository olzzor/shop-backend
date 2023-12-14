package com.bridgeshop.module.stats.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StatsSalesRequest {
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public StatsSalesRequest(String type, LocalDate startDate, LocalDate endDate) {
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
