package com.bridgeshop.batch.reader;

import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import com.bridgeshop.module.category.entity.Category;
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
public class StatsSalesCategoryItemReader extends JdbcCursorItemReader<StatsSalesCategory> {
    private boolean dataNoMore = false; // 더 이상 데이터가 없음을 표시하는 플래그
    private boolean dataEmptyReturned = false; // 빈 데이터 반환 여부


    public StatsSalesCategoryItemReader(DataSource dataSource) {
        // 데이터 소스 설정
        setDataSource(dataSource);

        // SQL 쿼리 설정
//        setSql(new StringBuilder()
//                .append("SELECT o.payment_amount, o.status, o.reg_date, p.category_id ")
//                .append("FROM orders o ")
//                .append("JOIN order_details od ON o.id = od.order_id ")
//                .append("JOIN products p ON od.product_id = p.id ")
//                .append("WHERE DATE(reg_date) = ? ")
//                .toString());
        setSql(new StringBuilder()
                .append("SELECT p.category_id AS category_id,")
                .append("       SUM(CASE WHEN o.status <> 'CANCEL_COMPLETED' THEN 1 ELSE 0 END) AS sold_order_count,")
                .append("       SUM(CASE WHEN o.status = 'CANCEL_COMPLETED' THEN 1 ELSE 0 END) AS canceled_order_count,")
                .append("       SUM(CASE WHEN o.status <> 'CANCEL_COMPLETED' THEN od.final_price ELSE 0 END) AS sold_amount,")
                .append("       SUM(CASE WHEN o.status = 'CANCEL_COMPLETED' THEN od.final_price ELSE 0 END) AS refund_amount ")
                .append("FROM orders o ")
                .append("JOIN order_details od ON o.id = od.order_id ")
                .append("JOIN products p ON od.product_id = p.id ")
                .append("WHERE DATE(o.reg_date) = ? ")
                .append("GROUP BY p.category_id")
                .toString());

        // PreparedStatementSetter를 사용하여 파라미터 바인딩
        setPreparedStatementSetter(new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                ps.setDate(1, Date.valueOf(yesterday));
            }
        });

        // RowMapper 설정: 데이터베이스 결과를 StatsSalesCategory 객체로 변환
        setRowMapper(new RowMapper<StatsSalesCategory>() {
            @Override
            public StatsSalesCategory mapRow(ResultSet rs, int rowNum) throws SQLException {

                StatsSalesCategory stats = new StatsSalesCategory();

                // 카테고리 ID를 가져온 후, 해당 ID로 Category 객체 생성 또는 조회
                Long categoryId = rs.getLong("category_id");
                Category category = new Category();
                category.setId(categoryId);

                stats.setReferenceDate(LocalDate.now().minusDays(1));
                stats.setCategory(category);
                stats.setSoldOrderCount(rs.getInt("sold_order_count"));
                stats.setCanceledOrderCount(rs.getInt("canceled_order_count"));
                stats.setSoldAmount(rs.getInt("sold_amount"));
                stats.setRefundAmount(rs.getInt("refund_amount"));

                return stats;
            }
        });
    }

    @Override
    public StatsSalesCategory read() throws Exception {

        if (dataNoMore) return null; // 더 이상 데이터가 없으면 null 반환

        StatsSalesCategory stats = super.read();

        if (stats != null) {
            // 조회 결과 데이터가 있는 경우
            return stats; // 해당 StatsSalesCategory 객체 반환
        } else {
            // 조회 결과 데이터가 없는 경우
            if (!dataEmptyReturned) {
                // 첫 조회에서 데이터가 없는 경우, 빈 StatsSalesCategory 객체 반환
                dataEmptyReturned = true;
                return new StatsSalesCategory();
            } else {
                // 첫 조회 이후 결과 데이터가 없는 경우 null 반환. 더 이상 데이터가 없음을 표시 설정.
                dataNoMore = true;
                return null;
            }
        }
    }
}