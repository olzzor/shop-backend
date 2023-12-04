package com.bridgeshop.module.order.entity;

public enum OrderStatus {
    ORDER_RECEIVED,         // 주문 접수
    ORDER_CONFIRMED,        // 주문 확인
    ORDER_FINALIZED,        // 주문 확정
    SHIPMENT_PREPARING,     // 배송 준비
    CANCEL_REQUESTED,       // 취소 요청
    CANCEL_COMPLETED,       // 취소 완료
}
