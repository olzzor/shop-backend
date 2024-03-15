package com.shop.module.contact.entity;

import com.shop.common.entity.BaseTimeEntity;
import com.shop.module.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "contacts")
public class Contact extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, length = 50)
    private String inquirerName;

    @Column(nullable = false, length = 50)
    private String inquirerEmail;

    @Column(length = 50)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ContactType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 5000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ContactStatus status;

    @Column(nullable = false)
    private Long ref;

    @Column(nullable = false)
    private int step;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Contact(User user, String inquirerName, String inquirerEmail, String orderNumber,
                   ContactType type, String title, String content, ContactStatus status, Long ref, int step) {
        this.user = user;
        this.inquirerName = inquirerName;
        this.inquirerEmail = inquirerEmail;
        this.orderNumber = orderNumber;
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = status;
        this.ref = ref;
        this.step = step;
    }

    // 설정자 메서드들
    public void setUser(User user) {
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
}
