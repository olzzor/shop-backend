package com.bridgeshop.module.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ContactListResponse {
    private List<ContactDto> contacts;
    private int totalPages;
}
