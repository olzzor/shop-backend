package com.shop.module.order.repository;

import com.shop.module.order.entity.Order;
import com.shop.module.order.dto.OrderListSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findByCondition(OrderListSearchRequest orderListSearchRequest, Pageable pageable);
}