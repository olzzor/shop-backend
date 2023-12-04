package com.bridgeshop.batch.processor;

import com.bridgeshop.module.stats.entity.StatsSales;
import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.order.entity.OrderStatus;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.time.LocalDate;

@Component
public class StatsSalesItemProcessor implements ItemProcessor<Order, StatsSales> {

    @Override
    public StatsSales process(Order order) throws Exception {
        if (ObjectUtils.isEmpty(order)) {
            // 주문 데이터가 없을 경우
            return StatsSales.builder()
                    .referenceDate(LocalDate.now().minusDays(1))
                    .soldOrderCount(0)
                    .canceledOrderCount(0)
                    .soldAmount(0)
                    .refundAmount(0)
                    .build();
        } else {
            if (order.getRegDate() == null) {
                // 주문 데이터가 없을 경우
                return StatsSales.builder()
                        .referenceDate(LocalDate.now().minusDays(1))
                        .soldOrderCount(0)
                        .canceledOrderCount(0)
                        .soldAmount(0)
                        .refundAmount(0)
                        .build();
            } else {
                // 주문 데이터가 있을 경우, 매출 집계
                return StatsSales.builder()
                        .referenceDate(LocalDate.now().minusDays(1))
                        .soldOrderCount(1)  // 각 주문마다 1
                        .canceledOrderCount(isOrderCanceled(order) ? 1 : 0) // 취소된 주문의 경우 1, 아니면 0
                        .soldAmount(order.getPaymentAmount())  // 판매 금액
                        .refundAmount(isOrderCanceled(order) ? order.getPaymentAmount() : 0)  // 취소 금액
                        .build();
            }
        }
    }

    private boolean isOrderCanceled(Order order) {
        return order.getStatus() == OrderStatus.CANCEL_REQUESTED
                || order.getStatus() == OrderStatus.CANCEL_COMPLETED;
    }
}
