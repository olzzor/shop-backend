package com.shop.module.notice.dto;

import com.shop.module.notice.entity.NoticeStatus;
import com.shop.module.notice.entity.NoticeType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeDto {
    private Long id;
    private NoticeType type;
    private String title;
    private String content;
    private NoticeStatus status;
    @JsonProperty("isSliderImage")
    private Boolean isSliderImage; // null 허용하도록 데이터 타입 변경 (boolean -> Boolean)
    @JsonProperty("isModalImage")
    private Boolean isModalImage; // null 허용하도록 데이터 타입 변경 (boolean -> Boolean)
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private List<NoticeImageDto> noticeImages;
    private NoticeImageDto noticeMainImage;
    private NoticeImageDto noticeSliderImage;
    private NoticeImageDto noticeModalImage;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public NoticeDto(Long id, NoticeType type, String title, String content, NoticeStatus status,
                     Boolean isSliderImage, Boolean isModalImage,
                     LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.content = content;
        this.status = status;
        this.isSliderImage = isSliderImage;
        this.isModalImage = isModalImage;
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // 설정자 메서드
    public void setId(Long id) {
        this.id = id;
    }

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

    public void setIsSliderImage(Boolean isSliderImage) {
        this.isSliderImage = isSliderImage;
    }

    public void setIsModalImage(Boolean isModalImage) {
        this.isModalImage = isModalImage;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }

    public void setNoticeImages(List<NoticeImageDto> noticeImageDtoList) {
        this.noticeImages = noticeImageDtoList;
    }

    public void setMainImage(NoticeImageDto mainImageDto) {
        this.noticeMainImage = mainImageDto;
    }

    public void setSliderImage(NoticeImageDto sliderImageDto) {
        this.noticeSliderImage = sliderImageDto;
    }

    public void setModalImage(NoticeImageDto modalImageDto) {
        this.noticeModalImage = modalImageDto;
    }
}
