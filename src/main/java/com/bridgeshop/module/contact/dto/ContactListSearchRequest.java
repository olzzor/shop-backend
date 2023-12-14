package com.bridgeshop.module.contact.dto;

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
    private String startRegDate;
    private String endRegDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ContactListSearchRequest(String title, String inquirerName, String inquirerEmail,
                                    String orderNumber, String type, String status,
                                    String startRegDate, String endRegDate) {
        this.title = title;
        this.inquirerName = inquirerName;
        this.inquirerEmail = inquirerEmail;
        this.orderNumber = orderNumber;
        this.type = type;
        this.status = status;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
    }
}
