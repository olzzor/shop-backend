package com.shop.module.contact.repository;

import com.shop.module.contact.dto.ContactListSearchRequest;
import com.shop.module.contact.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContactRepositoryCustom {
    Page<Contact> findByCondition(ContactListSearchRequest contactListSearchRequest, Pageable pageable);
}