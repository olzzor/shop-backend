package com.shop.module.review.dto;

import com.shop.module.order.dto.OrderDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.user.dto.UserDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
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

    // Builder pattern constructor
    @Builder
    public ReviewDto(Long id, byte rating, String title, String content, Boolean activateFlag,
                     LocalDateTime regDate, LocalDateTime modDate) {
        this.id = id;
        this.rating = rating;
        this.title = title;
        this.content = content;
        this.activateFlag = (activateFlag == null) ? true : activateFlag; // 기본값 설정
        this.regDate = regDate;
        this.modDate = modDate;
    }

    // Setter methods
    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(UserDto userDto) {
        this.user = userDto;
    }

    public void setOrder(OrderDto orderDto) {
        this.order = orderDto;
    }

    public void setRating(byte rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setActivateFlag(boolean activateFlag) {
        this.activateFlag = activateFlag;
    }

    public void setRegDate(LocalDateTime regDate) {
        this.regDate = regDate;
    }

    public void setModDate(LocalDateTime modDate) {
        this.modDate = modDate;
    }

    public void setReviewImages(List<ReviewImageDto> reviewImageDtoList) {
        this.reviewImages = reviewImageDtoList;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setProductSizes(List<ProductSizeDto> productSizeDtoList) {
        this.productSizes = productSizeDtoList;
    }
}
