package com.bridgeshop.module.notice.dto;

import com.bridgeshop.module.notice.entity.NoticeStatus;
import com.bridgeshop.module.notice.entity.NoticeType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    public NoticeDto includeMainImage(NoticeImageDto mainImageDto) {
        this.noticeMainImage = mainImageDto;
        return this;
    }

    public NoticeDto includeSliderImage(NoticeImageDto sliderImageDto) {
        this.noticeSliderImage = sliderImageDto;
        return this;
    }

    public NoticeDto includeModalImage(NoticeImageDto modalImageDto) {
        this.noticeModalImage = modalImageDto;
        return this;
    }
}
