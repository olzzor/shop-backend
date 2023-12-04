package com.bridgeshop.module.review.dto;

import com.bridgeshop.module.product.dto.ProductSizeDto;
import com.bridgeshop.module.user.dto.UserDto;
import com.bridgeshop.module.order.dto.OrderDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private UserDto user;
    private OrderDto order;
    private byte rating;
    private String title;
    private String content;
    private boolean activateFlag;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
    private List<ReviewImageDto> reviewImages;

    private String userEmail;
    private List<ProductSizeDto> productSizes;

    public ReviewDto includeReviewImages(List<ReviewImageDto> reviewImageDtoList) {
        this.reviewImages = reviewImageDtoList;
        return this;
    }

    public ReviewDto includeUserEmail(String userEmail) {
        this.userEmail = userEmail;
        return this;
    }

    public ReviewDto includeUser(UserDto userDto) {
        this.user = userDto;
        return this;
    }

    public ReviewDto includeProductSizes(List<ProductSizeDto> productSizeDtoList) {
        this.productSizes = productSizeDtoList;
        return this;
    }
}
