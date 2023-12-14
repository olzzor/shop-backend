package com.bridgeshop.module.review.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // 빌더 패턴을 사용하는 생성자
    @Builder
    public ReviewListSearchRequest(String id, String rating, String title, String content,
                                   String userEmail, String activateFlag, String startRegDate,
                                   String endRegDate, String startModDate, String endModDate) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.userEmail = userEmail;
        this.activateFlag = activateFlag;
        this.startRegDate = startRegDate;
        this.endRegDate = endRegDate;
        this.startModDate = startModDate;
        this.endModDate = endModDate;
    }
}
