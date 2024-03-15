package com.shop.module.notice.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeListSearchRequest {
    private String type;
    private String title;
    private String isSliderImage;
    private String isModalImage;
    private String status;
    private String startRegDate;
    private String endRegDate;
    private String startModDate;
    private String endModDate;

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public NoticeListSearchRequest(String type, String title, String isSliderImage,
                                   String isModalImage, String status, String startRegDate,
                                   String endRegDate, String startModDate, String endModDate) {
        this.type = type;
        this.title = title;
        this.isSliderImage = isSliderImage;
        this.isModalImage = isModalImage;
        this.status = status;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
        this.startModDate = startModDate;
        this.endModDate = endModDate;
    }
}
