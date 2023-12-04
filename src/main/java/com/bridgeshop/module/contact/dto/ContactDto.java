package com.bridgeshop.module.contact.dto;

import com.bridgeshop.module.contact.entity.ContactStatus;
import com.bridgeshop.module.contact.entity.ContactType;
import com.bridgeshop.module.user.dto.UserDto;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContactDto {
    private Long id;
    private UserDto user;
    private String inquirerName;
    private String inquirerEmail;
    private String orderNumber;
    private ContactType type;
    private String title;
    private String content;
    private ContactStatus status;
    private Long ref;
    private int step;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int countAnswer; // 문의에 대한 답변의 개수
}
