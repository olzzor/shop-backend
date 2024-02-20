package com.bridgeshop.module.order.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderPaymentRequest {
    private String payMethod;
    private String merchantUid;
    private String amount;
    private String buyerEmail;
    private String buyerName;
    private String buyerTel;
    private String buyerAddr;
    private String buyerPostcode;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public OrderPaymentRequest(String payMethod, String merchantUid, String amount, String buyerEmail,
                               String buyerName, String buyerTel, String buyerAddr, String buyerPostcode) {
        this.payMethod = payMethod;
        this.merchantUid = merchantUid;
        this.amount = amount;
        this.buyerEmail = buyerEmail;
        this.buyerName = buyerName;
        this.buyerTel = buyerTel;
        this.buyerAddr = buyerAddr;
        this.buyerPostcode = buyerPostcode;
    }
}
