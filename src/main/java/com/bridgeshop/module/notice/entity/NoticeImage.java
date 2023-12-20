package com.bridgeshop.module.notice.entity;

import com.bridgeshop.common.entity.BaseTimeEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "notice_images")
public class NoticeImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonManagedReference
    @JoinColumn(nullable = false, name = "notice_id", referencedColumnName = "id")
    private Notice notice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private NoticeImageType type;

    @Column(nullable = false, length = 512)
    private String fileUrl;

    @Column(nullable = false, length = 512)
    private String fileKey;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public NoticeImage(Notice notice, NoticeImageType type, String fileUrl, String fileKey) {
        this.notice = notice;
        this.type = type;
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
    }

    // 설정자 메서드들
    public void setNotice(Notice notice) {
        this.notice = notice;
    }

    public void setType(NoticeImageType type) {
        this.type = type;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
