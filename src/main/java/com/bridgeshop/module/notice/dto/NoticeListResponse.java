package com.bridgeshop.module.notice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeListResponse {
    private List<NoticeDto> notices;
    private int totalPages;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public NoticeListResponse(List<NoticeDto> notices, int totalPages) {
        this.notices = notices;
        this.totalPages = totalPages;
    }
}
