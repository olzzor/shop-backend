package com.bridgeshop.module.contact.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactUserResponse {
    private String userName;
    private String userEmail;
    private List<String> orderNumbers;
}
