package com.bridgeshop.module.order.repository;

import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.order.dto.OrderListSearchRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<Order> findByCondition(OrderListSearchRequest orderListSearchRequest, Pageable pageable);
}