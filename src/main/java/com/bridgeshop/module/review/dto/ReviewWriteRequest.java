package com.bridgeshop.module.review.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewWriteRequest {
    //    private Long orderDetailId;
    private Long orderId;
    private byte rating;
    private String title;
    private String content;
}
