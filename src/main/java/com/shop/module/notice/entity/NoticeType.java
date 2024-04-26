package com.shop.module.notice.entity;

public enum NoticeType {
    INFORMATION("정보"), // 일반 정보 공지
    COMMUNITY("커뮤니티"), // 커뮤니티 관련 공지
    PROMOTION("프로모션"), // 프로모션, 특별 행사, 경품 추첨 등의 행사나 이벤트에 관한 공지
    MAINTENANCE("유지보수"), // 시스템 관련 공지 및 서버 멘테넌스, 업데이트 공지
    ALERT("긴급"); // 긴급한 사안이나 중요한 변경 사항

    private final String description;

    NoticeType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
