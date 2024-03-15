package com.shop.module.review.mapper;

import com.shop.module.review.entity.ReviewImage;
import com.shop.module.review.dto.ReviewImageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ReviewImageMapper {

    public ReviewImageDto mapToDto(ReviewImage reviewImage) {
        return ReviewImageDto.builder()
                .id(reviewImage.getId())
                .fileUrl(reviewImage.getFileUrl())
                .fileKey(reviewImage.getFileKey())
                .displayOrder(reviewImage.getDisplayOrder())
                .build();
    }

    public List<ReviewImageDto> mapToDtoList(List<ReviewImage> reviewImageList) {
        return reviewImageList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}