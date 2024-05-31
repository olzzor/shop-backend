package com.shop.module.contact.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ContactListResponse {
    private List<ContactDto> contacts;
    private int totalPages;
    private Long totalContacts; // 전체 문의 수

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ContactListResponse(List<ContactDto> contacts, int totalPages, Long totalContacts) {
        this.contacts = contacts;
        this.totalPages = totalPages;
        this.totalContacts = totalContacts;
    }
}
