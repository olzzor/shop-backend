package com.bridgeshop.batch.reader;

import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.order.entity.OrderStatus;
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
public class StatsSalesItemReader extends JdbcCursorItemReader<Order> {
    private boolean dataNoMore = false; // 더 이상 데이터가 없음을 표시하는 플래그
    private boolean dataRead = false; // 데이터 조회 유무

    public StatsSalesItemReader(DataSource dataSource) {
        // 데이터 소스 설정
        setDataSource(dataSource);

        // SQL 쿼리 설정
        setSql(new StringBuilder()
                .append("SELECT * ")
                .append("FROM orders ")
                .append("WHERE DATE(reg_date) = ? ")
                .toString());

        // PreparedStatementSetter를 사용하여 파라미터 바인딩
        setPreparedStatementSetter(new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                LocalDate yesterday = LocalDate.now().minusDays(1);
                ps.setDate(1, Date.valueOf(yesterday));
            }
        });

//        setRowMapper(new BeanPropertyRowMapper<>(Order.class)); // RowMapper 설정: 데이터베이스 결과를 Order 객체로 변환
        setRowMapper(new RowMapper<Order>() {
            @Override
            public Order mapRow(ResultSet rs, int rowNum) throws SQLException {

                Order order = new Order();

                order.setPaymentAmount(rs.getInt("payment_amount"));
                order.setStatus(OrderStatus.valueOf(rs.getString("status")));
                order.setRegDate(rs.getTimestamp("reg_date").toLocalDateTime());

//                statsSales.setReferenceDate(LocalDate.now().minusDays(1));
//                statsSales.setSoldOrderCount(rs.getString("order_number"));
//                statsSales.setCanceledOrderCount(rs.getString("buyer_email"));
//                statsSales.setSoldAmount(rs.getString("payment_method"));
//                statsSales.setRefundAmount(rs.getInt("payment_amount"));

                return order;
            }
        });
    }

    @Override
    public Order read() throws Exception {

        if (dataNoMore) return null; // 더 이상 데이터가 없으면 null 반환

        Order order = super.read();

        if (order != null) {
            // 조회 결과 데이터가 있는 경우
            dataRead = true;
            return order; // 해당 order 객체 반환
        } else {
            // 조회 결과 데이터가 없는 경우
            if (!dataRead) {
                // 첫 조회에서 데이터가 없는 경우, 빈 Order 객체 반환
                dataRead = true;
                return new Order();
            } else {
                // 첫 조회 이후 결과 데이터가 없는 경우 null 반환. 더 이상 데이터가 없음을 표시 설정.
                dataNoMore = true;
                return null;
            }
        }
    }
}