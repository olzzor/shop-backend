package com.shop.module.contact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ContactListSearchRequest {
    private String title;
    private String inquirerName;
    private String inquirerEmail;
    private String orderNumber;
    private String type;
    private String status;
    @JsonProperty("isPrivate")
    private String isPrivate;   // 추가
    private String productName; // 추가
    private String startRegDate;
    private String endRegDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ContactListSearchRequest(String title, String inquirerName, String inquirerEmail,
                                    String orderNumber, String type, String status,
                                    String isPrivate, String productName,
                                    String startRegDate, String endRegDate) {
        this.title = title;
        this.inquirerName = inquirerName;
        this.inquirerEmail = inquirerEmail;
        this.orderNumber = orderNumber;
        this.type = type;
        this.status = status;
        this.isPrivate = isPrivate;
        this.productName = productName;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
    }
}
