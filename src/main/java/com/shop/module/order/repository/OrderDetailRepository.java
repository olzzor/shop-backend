package com.shop.module.order.repository;

import com.shop.module.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findAllByShipment_id(Long shipmentId);

    List<OrderDetail> findAllByOrder_id(Long orderId);
}
