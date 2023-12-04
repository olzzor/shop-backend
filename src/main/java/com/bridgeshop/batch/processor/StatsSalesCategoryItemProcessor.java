package com.bridgeshop.batch.processor;

import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StatsSalesCategoryItemProcessor implements ItemProcessor<StatsSalesCategory, StatsSalesCategory> {

    @Override
    public StatsSalesCategory process(StatsSalesCategory stats) throws Exception {
//        if (!ObjectUtils.isEmpty(stats)) {
//            return stats;
//        }

        return stats;

//        return StatsSalesCategory.builder()
//                .referenceDate(LocalDate.now().minusDays(1))
//                .soldOrderCount(0)
//                .canceledOrderCount(0)
//                .soldAmount(0)
//                .refundAmount(0)
//                .build();

    }
}
