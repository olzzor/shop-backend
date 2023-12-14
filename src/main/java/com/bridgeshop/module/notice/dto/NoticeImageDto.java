package com.bridgeshop.module.notice.dto;

import com.bridgeshop.module.notice.entity.NoticeImageType;
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
    private String filePath;
    private String fileName;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public NoticeImageDto(Long id, NoticeImageType type, String filePath, String fileName) {
        this.id = id;
        this.type = type;
        this.filePath = filePath;
        this.fileName = fileName;
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

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
