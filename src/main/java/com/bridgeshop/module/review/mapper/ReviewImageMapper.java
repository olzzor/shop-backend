package com.bridgeshop.module.review.mapper;

import com.bridgeshop.module.review.entity.ReviewImage;
import com.bridgeshop.module.review.dto.ReviewImageDto;
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
                .filePath(reviewImage.getFilePath())
                .fileName(reviewImage.getFileName())
                .displayOrder(reviewImage.getDisplayOrder())
                .build();
    }

    public List<ReviewImageDto> mapToDtoList(List<ReviewImage> reviewImageList) {
        return reviewImageList.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }
}