package com.shop.batch.writer;

import com.shop.module.stats.entity.StatsSales;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;

@Component
public class StatsSalesItemWriter implements ItemWriter<StatsSales> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatsSalesItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void write(Chunk<? extends StatsSales> items) throws Exception {

        LocalDate referenceDate = LocalDate.now().minusDays(1);

        if (!isDataAlreadyPresent(referenceDate)) {
            if (!items.isEmpty()) {
                // 매출 데이터가 있는 경우 저장
                saveStatsSales(items.getItems().get(0));
            } else {
                // 매출 데이터가 없는 경우 새 객체 생성 및 저장
                StatsSales noSalesData = StatsSales.builder()
                        .referenceDate(LocalDate.now().minusDays(1))
                        .soldOrderCount(0)
                        .canceledOrderCount(0)
                        .soldAmount(0)
                        .refundAmount(0)
                        .build();
                saveStatsSales(noSalesData);
            }
        }
    }

    private void saveStatsSales(StatsSales stats) {
        String sql = "INSERT INTO stats_sales (reference_date, sold_order_count, canceled_order_count, sold_amount, refund_amount) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, stats.getReferenceDate(), stats.getSoldOrderCount(), stats.getCanceledOrderCount(), stats.getSoldAmount(), stats.getRefundAmount());
    }

    private boolean isDataAlreadyPresent(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM stats_sales WHERE reference_date = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{date}, Integer.class);
        return count > 0;
    }
}