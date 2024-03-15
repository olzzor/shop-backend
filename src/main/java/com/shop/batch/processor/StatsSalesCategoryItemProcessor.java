package com.shop.batch.processor;

import com.shop.module.stats.entity.StatsSalesCategory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StatsSalesCategoryItemProcessor implements ItemProcessor<StatsSalesCategory, StatsSalesCategory> {

    @Override
    public StatsSalesCategory process(StatsSalesCategory stats) {
        return stats;
    }
}
