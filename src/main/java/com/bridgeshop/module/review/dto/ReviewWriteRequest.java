package com.bridgeshop.module.review.dto;

import lombok.Getter;

@Getter
public class ReviewWriteRequest {
    //    private Long orderDetailId;
    private Long orderId;
    private byte rating;
    private String title;
    private String content;
}
