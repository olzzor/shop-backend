package com.shop.module.contact.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shop.module.contact.entity.ContactStatus;
import com.shop.module.contact.entity.ContactType;
import com.shop.module.product.dto.ProductDto;
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
    private ProductDto product;
    private ContactType type;
    private String title;
    private String content;
    private ContactStatus status;
    private Long ref;
    private int step;
    @JsonProperty("isPrivate")
    private boolean isPrivate;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private int countAnswer; // 문의에 대한 답변의 개수

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ContactDto(Long id, String inquirerName, String inquirerEmail, String orderNumber,
                      ContactType type, String title, String content, ContactStatus status,
                      Long ref, int step, boolean isPrivate,
                      LocalDateTime regDate, LocalDateTime modDate, int countAnswer) {
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
        this.isPrivate = isPrivate;
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

    public void setProduct(ProductDto product) {
        this.product = product;
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

    public void setPrivate(boolean isPrivate) {
        this.isPrivate = isPrivate;
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
