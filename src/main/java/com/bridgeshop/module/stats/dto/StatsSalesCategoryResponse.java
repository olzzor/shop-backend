package com.bridgeshop.module.stats.dto;

import com.bridgeshop.module.category.entity.Category;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatsSalesCategoryResponse {
//    private String type;
//    private LocalDate startDate;
//    private LocalDate endDate;
    private Category category;
    private int soldOrderCount;
    private int canceledOrderCount;
    private int soldAmount;
    private int refundAmount;
}
