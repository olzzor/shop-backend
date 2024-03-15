package com.shop.module.shipment.repository;

import com.shop.module.order.entity.QOrder;
import com.shop.module.order.entity.QOrderDetail;
import com.shop.module.product.entity.QProduct;
import com.shop.module.shipment.dto.ShipmentListSearchRequest;
import com.shop.module.shipment.entity.QShipment;
import com.shop.module.shipment.entity.Shipment;
import com.shop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


public class ShipmentRepositoryCustomImpl implements ShipmentRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Shipment> findByCondition(ShipmentListSearchRequest shipmentListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QShipment qShipment = QShipment.shipment;
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QOrder qOrder = QOrder.order;
        QProduct qProduct = QProduct.product;

        JPAQuery<Shipment> query = queryFactory
                .selectFrom(qShipment).distinct()
                .leftJoin(qShipment.orderDetails, qOrderDetail)
                .leftJoin(qOrderDetail.order, qOrder)
                .leftJoin(qOrderDetail.product, qProduct)
                .where(
                        QueryDslUtils.eqCourierCompany(qShipment.courierCompany, shipmentListSearchRequest.getCourierCompany()),
                        QueryDslUtils.likeString(qShipment.trackingNumber, shipmentListSearchRequest.getTrackingNumber()),
                        QueryDslUtils.eqShipmentStatus(qShipment.status, shipmentListSearchRequest.getShipmentStatus()),
                        QueryDslUtils.likeString(qShipment.recipientName, shipmentListSearchRequest.getRecipientName()),
                        QueryDslUtils.likeString(qShipment.recipientPhone, shipmentListSearchRequest.getRecipientPhone()),
                        QueryDslUtils.likeString(qShipment.shippingAddress, shipmentListSearchRequest.getShippingAddress()),
                        QueryDslUtils.likeString(qOrder.orderNumber, shipmentListSearchRequest.getOrderNumber()),
                        QueryDslUtils.likeString(qProduct.name, shipmentListSearchRequest.getOrderProduct()),
                        QueryDslUtils.betweenDate(qShipment.regDate, shipmentListSearchRequest.getStartRegDate(), shipmentListSearchRequest.getEndRegDate()),
                        QueryDslUtils.betweenDate(qShipment.modDate, shipmentListSearchRequest.getStartModDate(), shipmentListSearchRequest.getEndModDate())
                )
                .orderBy(qShipment.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Shipment> shipmentList = query.fetch();
        long totalCount = query.fetchCount(); // 전체 카운트 취득

        return new PageImpl<>(shipmentList, pageable, totalCount);
    }
}