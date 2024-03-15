package com.shop.module.order.repository;

import com.shop.module.order.dto.OrderListSearchRequest;
import com.shop.module.order.entity.Order;
import com.shop.module.order.entity.QOrder;
import com.shop.module.order.entity.QOrderDetail;
import com.shop.module.product.entity.QProduct;
import com.shop.common.util.QueryDslUtils;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class OrderRepositoryCustomImpl implements OrderRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<Order> findByCondition(OrderListSearchRequest orderListSearchRequest, Pageable pageable) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QOrder qOrder = QOrder.order;
        QOrderDetail qOrderDetail = QOrderDetail.orderDetail;
        QProduct qProduct = QProduct.product;

        JPAQuery<Order> query = queryFactory
                .selectFrom(qOrder).distinct()
                .leftJoin(qOrder.orderDetails, qOrderDetail)
                .leftJoin(qOrderDetail.product, qProduct)
                .where(
                        QueryDslUtils.likeString(qOrder.orderNumber, orderListSearchRequest.getOrderNumber()),
                        QueryDslUtils.likeString(qOrder.buyerEmail, orderListSearchRequest.getBuyerEmail()),
                        QueryDslUtils.likeString(qOrder.paymentMethod, orderListSearchRequest.getPaymentMethod()),
                        QueryDslUtils.eqInteger(qOrder.paymentAmount, orderListSearchRequest.getPaymentAmount()),
                        QueryDslUtils.likeString(qProduct.name, orderListSearchRequest.getOrderProduct()),
                        QueryDslUtils.eqOrderStatus(qOrder.status, orderListSearchRequest.getOrderStatus()),
                        QueryDslUtils.betweenDate(qOrder.regDate, orderListSearchRequest.getStartOrderDate(), orderListSearchRequest.getEndOrderDate())
                )
                .orderBy(qOrder.regDate.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        List<Order> orderList = query.fetch();
        long totalCount = query.fetchCount(); // 전체 카운트 취득

        return new PageImpl<>(orderList, pageable, totalCount);
    }

//    private BooleanExpression eqIfNotZero(NumberPath<Integer> path, int value) {
//        return (value != 0) ? path.eq(value) : null;
//    }
}