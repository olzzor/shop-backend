package com.shop.module.contact.dto;

import com.shop.module.contact.entity.ContactStatus;
import com.shop.module.contact.entity.ContactType;
import com.shop.module.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ContactDto {
    private Long id;
    private UserDto user;
    private String inquirerName;
    private String inquirerEmail;
    private String orderNumber;
    private ContactType type;
    private String title;
    private String content;
    private ContactStatus status;
    private Long ref;
    private int step;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int countAnswer; // 문의에 대한 답변의 개수

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ContactDto(Long id, String inquirerName, String inquirerEmail, String orderNumber,
                      ContactType type, String title, String content, ContactStatus status,
                      Long ref, int step, LocalDateTime regDate, LocalDateTime modDate, int countAnswer) {
        this.id = id;
        this.inquirerName = inquirerName;
        this.inquirerEmail = inquirerEmail;
        this.orderNumber = orderNumber;
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = status;
        this.ref = ref;
        this.step = step;
        this.regDate = regDate;
        this.modDate = modDate;
        this.countAnswer = countAnswer;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public void setInquirerName(String inquirerName) {
        this.inquirerName = inquirerName;
    }

    public void setInquirerEmail(String inquirerEmail) {
        this.inquirerEmail = inquirerEmail;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setType(ContactType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(ContactStatus status) {
        this.status = status;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }

    public void setCountAnswer(int countAnswer) {
        this.countAnswer = countAnswer;
    }
}
