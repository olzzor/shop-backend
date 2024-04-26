package com.shop.module.notice.entity;

public enum NoticeStatus {
    /**
     * 활성 상태
     * 공지사항이 현재 사용자에게 보여지고 있는 상태입니다.
     * 해당 공지사항이 유효한 기간 동안 화면에 표시됩니다.
     */
    ACTIVE("활성"),

    /**
     * 비활성 상태
     * 공지사항이 일시적으로 사용자에게 보여지지 않습니다.
     * 관리자가 임의로 비활성화하여 사용자에게 숨길 경우 사용됩니다.
     */
    INACTIVE("비활성"),

    /**
     * 만료 상태
     * 공지사항의 유효 기간이 지난 상태입니다.
     * 설정된 날짜가 지나면 자동으로 이 상태로 변경될 수 있습니다.
     */
    EXPIRED("만료"),

    /**
     * 삭제 상태 (논리)
     * 공지사항이 삭제되었지만, 데이터베이스에서는 실제로 삭제되지 않고 상태만 변경되어 보관되는 상태입니다.
     * 나중에 복구가 필요할 경우를 대비해 물리적으로 삭제하지 않고 상태만 변경하여 보관합니다.
     * 관리자만 확인 가능합니다.
     */
    DELETED("삭제");

    private final String description;

    NoticeStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
