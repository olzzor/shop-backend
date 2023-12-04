package com.bridgeshop.module.review.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewImageDto {
    private Long id;
    private ReviewDto review;
    private String filePath;
    private String fileName;
    private int displayOrder;
}
