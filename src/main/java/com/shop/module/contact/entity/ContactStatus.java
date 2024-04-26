package com.shop.module.contact.entity;

public enum ContactStatus {
    UNANSWERED("답변 대기"),
    ANSWERED("답변 완료"),
    CLOSED("문의 완료");

    private final String description;

    ContactStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
