package com.bridgeshop.module.notice.repository;

import com.bridgeshop.module.notice.dto.NoticeListSearchRequest;
import com.bridgeshop.module.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> findByCondition(NoticeListSearchRequest noticeListSearchRequest, Pageable pageable);
}