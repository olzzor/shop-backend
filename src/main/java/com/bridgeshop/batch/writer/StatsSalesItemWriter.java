package com.bridgeshop.batch.writer;

import com.bridgeshop.module.stats.entity.StatsSales;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;

@Component
public class StatsSalesItemWriter extends JdbcBatchItemWriter<StatsSales> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StatsSalesItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        setDataSource(dataSource);
        // SQL 쿼리 설정
        setSql("INSERT INTO stats_sales (reference_date, sold_order_count, canceled_order_count, sold_amount, refund_amount) VALUES (:referenceDate, :soldOrderCount, :canceledOrderCount, :soldAmount, :refundAmount)");
        // SqlParameterSourceProvider를 설정하여 StatsSales 객체의 필드를 SQL 파라미터와 매핑
        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    }

    @Override
    public void write(Chunk<? extends StatsSales> items) throws Exception {

        LocalDate referenceDate = LocalDate.now().minusDays(1);

        if (!isDataAlreadyPresent(referenceDate)) {
            StatsSales statsSales = new StatsSales(); // StatsSales 객체 초기화

            for (StatsSales item : items) {
                // 주문 건수, 취소 건수, 판매 금액, 환불 금액 등을 집계
                statsSales.addSoldOrderCount(item.getSoldOrderCount());
                statsSales.addCanceledOrderCount(item.getCanceledOrderCount());
                statsSales.addSoldAmount(item.getSoldAmount());
                statsSales.addRefundAmount(item.getRefundAmount());
            }

            // 집계된 단일 결과를 데이터베이스에 저장
            Chunk<StatsSales> chunk = new Chunk<>();
            chunk.add(statsSales);
            super.write(chunk);
        }
    }

    private boolean isDataAlreadyPresent(LocalDate date) {
        String sql = "SELECT COUNT(*) FROM stats_sales_category WHERE reference_date = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{date}, Integer.class);
        return count > 0;
    }
}