package com.shop.module.notice.repository;

import com.shop.module.notice.dto.NoticeListSearchRequest;
import com.shop.module.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NoticeRepositoryCustom {
    Page<Notice> findByCondition(NoticeListSearchRequest noticeListSearchRequest, Pageable pageable);
}