package com.bridgeshop.module.notice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
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
}
