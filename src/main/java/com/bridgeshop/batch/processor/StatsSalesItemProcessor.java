package com.bridgeshop.batch.processor;

import com.bridgeshop.module.stats.entity.StatsSales;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StatsSalesItemProcessor implements ItemProcessor<StatsSales, StatsSales> {

    @Override
    public StatsSales process(StatsSales stats) {
        return stats;
    }
}
