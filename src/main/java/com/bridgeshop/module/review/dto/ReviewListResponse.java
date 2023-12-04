package com.bridgeshop.module.review.dto;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponse {
    private List<ReviewDto> reviews;
    private int totalPages;
}