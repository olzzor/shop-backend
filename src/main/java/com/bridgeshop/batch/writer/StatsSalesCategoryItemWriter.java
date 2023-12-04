package com.bridgeshop.batch.writer;

import com.bridgeshop.module.stats.entity.StatsSalesCategory;
import com.bridgeshop.module.category.entity.Category;
import com.bridgeshop.module.category.service.CategoryService;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
//    public class StatsSalesCategoryItemWriter extends JdbcBatchItemWriter<StatsSalesCategory> {
public class StatsSalesCategoryItemWriter implements ItemWriter<StatsSalesCategory> {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    public StatsSalesCategoryItemWriter(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//
//        setDataSource(dataSource);
//        // SQL 쿼리 설정
//        setSql("INSERT INTO stats_sales_category (reference_date, category_id, sold_order_count, canceled_order_count, sold_amount, refund_amount) VALUES (:referenceDate, :category.id, :soldOrderCount, :canceledOrderCount, :soldAmount, :refundAmount)");
//        // SqlParameterSourceProvider를 설정하여 StatsSales 객체의 필드를 SQL 파라미터와 매핑
//        setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
    }

    @Override
    public void write(Chunk<? extends StatsSalesCategory> items) throws Exception {

        LocalDate referenceDate = LocalDate.now().minusDays(1);
        List<Category> allCategories = categoryService.getAll();

        // 모든 카테고리를 순회
        for (Category category : allCategories) {
            if (!isDataAlreadyPresent(referenceDate, category.getId())) {
                // 현재 카테고리에 대한 매출 데이터가 있는지 검사
                Optional<? extends StatsSalesCategory> statsOptional = items.getItems().stream()
                        .filter(item -> Objects.equals(item.getCategory(), category))
                        .findFirst();

                if (statsOptional.isPresent()) {
                    // 매출 데이터가 있는 경우 저장
                    saveStatsSalesCategory(statsOptional.get());
                } else {
                    // 매출 데이터가 없는 경우 새 객체 생성 및 저장
                    StatsSalesCategory noSalesData = StatsSalesCategory.builder()
                            .referenceDate(LocalDate.now().minusDays(1))
                            .category(category)
                            .soldOrderCount(0)
                            .canceledOrderCount(0)
                            .soldAmount(0)
                            .refundAmount(0)
                            .build();
                    saveStatsSalesCategory(noSalesData);
                }
            }
        }
    }

    private void saveStatsSalesCategory(StatsSalesCategory stats) {
        String sql = "INSERT INTO stats_sales_category (reference_date, category_id, sold_order_count, canceled_order_count, sold_amount, refund_amount) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, stats.getReferenceDate(), stats.getCategory().getId(), stats.getSoldOrderCount(), stats.getCanceledOrderCount(), stats.getSoldAmount(), stats.getRefundAmount());
    }

    private boolean isDataAlreadyPresent(LocalDate date, Long categoryId) {
        String sql = "SELECT COUNT(*) FROM stats_sales_category WHERE reference_date = ? AND category_id = ?";
        int count = jdbcTemplate.queryForObject(sql, new Object[]{date, categoryId}, Integer.class);
        return count > 0;
    }
//    @Override
//    public void write(Chunk<? extends StatsSalesCategory> items) throws Exception {
//        // 단일 집계 객체 초기화
//        StatsSalesCategory statsSalesCategory = new StatsSalesCategory();
//
//        for (StatsSalesCategory item : items) {
//            // 주문 건수, 취소 건수, 판매 금액, 환불 금액 등을 집계
//            statsSalesCategory.addSoldOrderCount(item.getSoldOrderCount());
//            statsSalesCategory.addCanceledOrderCount(item.getCanceledOrderCount());
//            statsSalesCategory.addSoldAmount(item.getSoldAmount());
//            statsSalesCategory.addRefundAmount(item.getRefundAmount());
//        }
//
//        // 집계된 단일 결과를 데이터베이스에 저장
//        Chunk<StatsSales> chunk = new Chunk<>();
//        chunk.add(statsSalesCategory);
//        super.write(chunk);
//    }
}