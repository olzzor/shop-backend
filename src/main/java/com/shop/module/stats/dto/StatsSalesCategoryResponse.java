package com.shop.module.stats.dto;

import com.shop.module.category.entity.Category;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class StatsSalesCategoryResponse {
//    private String type;
//    private LocalDate startDate;
//    private LocalDate endDate;
    private Category category;
    private int soldOrderCount;
    private int canceledOrderCount;
    private int soldAmount;
    private int refundAmount;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public StatsSalesCategoryResponse(int soldOrderCount, int canceledOrderCount,
                                      int soldAmount, int refundAmount) {
        this.soldOrderCount = soldOrderCount;
        this.canceledOrderCount = canceledOrderCount;
        this.soldAmount = soldAmount;
        this.refundAmount = refundAmount;
    }
}
