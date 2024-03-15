package com.shop.batch.reader;

import com.shop.module.stats.entity.StatsSales;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Slf4j
@Component
public class StatsSalesItemReader extends JdbcCursorItemReader<StatsSales> {
    private boolean dataNoMore = false; // 더 이상 데이터가 없음을 표시하는 플래그
    private boolean dataRead = false; // 데이터 조회 유무

    public StatsSalesItemReader(DataSource dataSource) {
        // 데이터 소스 설정
        setDataSource(dataSource);

        // SQL 쿼리 설정
        setSql(new StringBuilder()
                .append("SELECT SUM(CASE WHEN o.status <> 'CANCEL_COMPLETED' THEN 1 ELSE 0 END) AS sold_order_count,")
                .append("       SUM(CASE WHEN o.status = 'CANCEL_COMPLETED' THEN 1 ELSE 0 END) AS canceled_order_count,")
                .append("       SUM(CASE WHEN o.status <> 'CANCEL_COMPLETED' THEN od.final_price ELSE 0 END) AS sold_amount,")
                .append("       SUM(CASE WHEN o.status = 'CANCEL_COMPLETED' THEN od.final_price ELSE 0 END) AS refund_amount ")
                .append("FROM orders o ")
                .append("JOIN order_details od ON o.id = od.order_id ")
                .append("WHERE DATE(o.reg_date) = ? ")
                .toString());

        setPreparedStatementSetter(new PreparedStatementSetter() { // 파라미터 바인딩
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                ps.setDate(1, Date.valueOf(yesterday));
            }
        });

        setRowMapper(new RowMapper<StatsSales>() { // 데이터베이스 결과를 StatsSales 객체에 설정
            @Override
            public StatsSales mapRow(ResultSet rs, int rowNum) throws SQLException {

                StatsSales stats = StatsSales.builder()
                        .referenceDate(LocalDate.now().minusDays(1))
                        .soldOrderCount(rs.getInt("sold_order_count"))
                        .canceledOrderCount(rs.getInt("canceled_order_count"))
                        .soldAmount(rs.getInt("sold_amount"))
                        .refundAmount(rs.getInt("refund_amount"))
                        .build();

                return stats;
            }
        });
    }

    @Override
    public StatsSales read() throws Exception {

        if (dataNoMore) return null; // 더 이상 데이터가 없으면 null 반환

        StatsSales stats = super.read();

        if (stats != null) {
            // 조회 결과 데이터가 있는 경우
            dataRead = true;
            return stats; // 해당 StatsSales 객체 반환
        } else {
            // 조회 결과 데이터가 없는 경우
            if (!dataRead) {
                // 첫 조회에서 데이터가 없는 경우, 빈 Order 객체 반환
                dataRead = true;
                return StatsSales.builder().build();
            } else {
                // 첫 조회 이후 결과 데이터가 없는 경우 null 반환. 더 이상 데이터가 없음을 표시 설정.
                dataNoMore = true;
                return null;
            }
        }
    }
}