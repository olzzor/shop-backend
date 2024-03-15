package com.shop.module.review.mapper;

import com.shop.module.review.entity.Review;
import com.shop.module.review.dto.ReviewDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewMapper {

    public ReviewDto mapToDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId())
                .rating(review.getRating())
                .title(review.getTitle())
                .content(review.getContent())
                .activateFlag(review.isActivateFlag())
                .regDate(review.getRegDate())
                .modDate(review.getModDate())
                .build();
    }

    public List<ReviewDto> mapToDtoList(List<Review> reviewList) {
        return reviewList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}