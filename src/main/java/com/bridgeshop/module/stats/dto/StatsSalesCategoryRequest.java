package com.bridgeshop.module.stats.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatsSalesCategoryRequest {
    private LocalDate startDate;
    private LocalDate endDate;
}
