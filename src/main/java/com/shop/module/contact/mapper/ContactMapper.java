package com.shop.module.contact.mapper;

import com.shop.module.contact.dto.ContactDto;
import com.shop.module.contact.entity.Contact;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ContactMapper {

    public ContactDto mapToDto(Contact contact) {
        return ContactDto.builder()
                .id(contact.getId())
                .inquirerName(contact.getInquirerName())
                .inquirerEmail(contact.getInquirerEmail())
                .orderNumber(contact.getOrderNumber())
                .type(contact.getType())
                .title(contact.getTitle())
                .content(contact.getContent())
                .status(contact.getStatus())
                .ref(contact.getRef())
                .step(contact.getStep())
                .isPrivate(contact.isPrivate())
                .regDate(contact.getRegDate())
                .modDate(contact.getModDate())
                .build();
    }

    public List<ContactDto> mapToDtoList(List<Contact> contactList) {
        return contactList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}