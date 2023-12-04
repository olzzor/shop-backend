package com.bridgeshop.module.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListSearchRequest {
    private String id;
    private String rating;
    private String title;
    private String content;
    private String userEmail;
    private String activateFlag;
    private String startRegDate;
    private String endRegDate;
    private String startModDate;
    private String endModDate;
}
