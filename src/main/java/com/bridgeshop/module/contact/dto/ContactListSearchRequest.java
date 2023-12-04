package com.bridgeshop.module.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactListSearchRequest {
    private String title;
    private String inquirerName;
    private String inquirerEmail;
    private String orderNumber;
    private String type;
    private String status;
    private String startRegDate;
    private String endRegDate;
}
