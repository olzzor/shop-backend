package com.bridgeshop.module.notice.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "notices")
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NoticeType type;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 10000)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private NoticeStatus status;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isSliderImage = false;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isModalImage = false;

    @OneToMany(mappedBy = "notice")
    @JsonBackReference
    private List<NoticeImage> noticeImages = new ArrayList<>();

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public Notice(NoticeType type, String title, String content, NoticeStatus status,
                  Boolean isSliderImage, Boolean isModalImage) {
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = status;
        this.isSliderImage = (isSliderImage == null) ? false : isSliderImage; // 기본값 설정
        this.isModalImage = (isModalImage == null) ? false : isModalImage; // 기본값 설정
    }

    // 설정자 메서드들
    public void setType(NoticeType type) {
        this.type = type;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(NoticeStatus status) {
        this.status = status;
    }

    public void setSliderImage(boolean isSliderImage) {
        this.isSliderImage = isSliderImage;
    }

    public void setModalImage(boolean isModalImage) {
        this.isModalImage = isModalImage;
    }
}
