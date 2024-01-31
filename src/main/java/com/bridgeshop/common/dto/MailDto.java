package com.bridgeshop.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class MailDto {
    private String address;
    private String title;
    private String content;
    private String template;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public MailDto(String address, String title, String content, String template) {
        this.address = address;
        this.title = title;
        this.content = content;
        this.template = template;
    }

    // 설정자 메서드
    public void setAddress(String address) {
        this.address = address;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTemplate(String template) {
        this.template = template;
    }
}