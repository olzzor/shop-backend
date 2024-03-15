package com.shop.module.notice.dto;

import com.shop.module.notice.entity.NoticeImageType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeImageDto {
    private Long id;
    private NoticeImageType type;
    private NoticeDto notice;
    private String fileUrl;
    private String fileKey;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public NoticeImageDto(Long id, NoticeImageType type, String fileUrl, String fileKey) {
        this.id = id;
        this.type = type;
        this.fileUrl = fileUrl;
        this.fileKey = fileKey;
    }

    // 설정자 메서드들
    public void setId(Long id) {
        this.id = id;
    }

    public void setType(NoticeImageType type) {
        this.type = type;
    }

    public void setNotice(NoticeDto noticeDto) {
        this.notice = noticeDto;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }
}
