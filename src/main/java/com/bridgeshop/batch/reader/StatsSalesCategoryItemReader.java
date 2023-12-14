package com.bridgeshop.batch.reader;

import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.module.category.entity.Category;
import com.bridgeshop.module.category.repository.CategoryRepository;
import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final CategoryRepository categoryRepository;
    private boolean dataNoMore = false; // 더 이상 데이터가 없음을 표시하는 플래그
    private boolean dataRead = false; // 데이터 조회 유무

    public StatsSalesCategoryItemReader(DataSource dataSource, CategoryRepository categoryRepository) {
        // 데이터 소스 설정
        setDataSource(dataSource);

        // CategoryRepository 주입
        this.categoryRepository = categoryRepository;

        // SQL 쿼리 설정
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

                // 카테고리 ID를 가져온 후, 해당 ID로 Category 객체 생성 또는 조회
                Long categoryId = rs.getLong("category_id");
                Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new NotFoundException("categoryNotFound", "카테고리 정보를 찾을 수 없습니다."));

                StatsSalesCategory stats = StatsSalesCategory.builder()
                        .referenceDate(LocalDate.now().minusDays(1))
                        .category(category)
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
    public StatsSalesCategory read() throws Exception {

        if (dataNoMore) return null; // 더 이상 데이터가 없으면 null 반환

        StatsSalesCategory stats = super.read();

        if (stats != null) {
            // 조회 결과 데이터가 있는 경우
            dataRead = true;
            return stats; // 해당 StatsSalesCategory 객체 반환
        } else {
            // 조회 결과 데이터가 없는 경우
            if (!dataRead) {
                // 첫 조회에서 데이터가 없는 경우, 빈 StatsSalesCategory 객체 반환
                dataRead = true;
                return StatsSalesCategory.builder().build();
            } else {
                // 첫 조회 이후 결과 데이터가 없는 경우 null 반환. 더 이상 데이터가 없음을 표시 설정.
                dataNoMore = true;
                return null;
            }
        }
    }
}