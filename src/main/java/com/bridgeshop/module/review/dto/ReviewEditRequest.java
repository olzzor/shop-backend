package com.bridgeshop.module.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewEditRequest {
    private Long reviewId;
    private byte rating;
    private String title;
    private String content;
}
