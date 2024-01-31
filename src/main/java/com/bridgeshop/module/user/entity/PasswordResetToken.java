package com.bridgeshop.module.user.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "password_reset_tokens")
public class PasswordResetToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @Column(nullable = false, length = 255)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public PasswordResetToken(User user, String token, LocalDateTime expDate) {
        this.user = user;
        this.token = token;
        this.expDate = expDate;
    }

    // 설정자 메서드들
    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setExpDate(LocalDateTime expDate) {
        this.expDate = expDate;
    }
}
