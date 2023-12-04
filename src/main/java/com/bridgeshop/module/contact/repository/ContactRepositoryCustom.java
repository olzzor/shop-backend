package com.bridgeshop.module.contact.repository;

import com.bridgeshop.module.contact.dto.ContactListSearchRequest;
import com.bridgeshop.module.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepositoryCustom {
    Page<Contact> findByCondition(ContactListSearchRequest contactListSearchRequest, Pageable pageable);
}