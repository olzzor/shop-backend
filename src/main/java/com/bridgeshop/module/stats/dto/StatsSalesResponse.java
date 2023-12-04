package com.bridgeshop.module.stats.dto;

import lombok.*;

import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsSalesResponse {
    //    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private int soldOrderCount;
    private int canceledOrderCount;
    private int soldAmount;
    private int refundAmount;
}
