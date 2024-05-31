package com.shop.module.product.controller;

import com.shop.common.exception.UnauthorizedException;
import com.shop.module.product.dto.ProductDetailDto;
import com.shop.module.product.entity.ProductDetail;
import com.shop.module.product.service.ProductDetailService;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product-detail")
public class ProductDetailController {

    private final JwtService jwtService;
    private final ProductDetailService productDetailService;

    /**
     * 상품 상세 내용 취득
     */
    @GetMapping("/content/{id}")
    public ResponseEntity getContent(@PathVariable("id") Long productDetailId,
                                     @CookieValue(value = "token", required = false) String accessToken,
                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }
        ProductDetail productDetail = productDetailService.retrieveById(productDetailId);
        ProductDetailDto productDetailDto = productDetailService.convertToDto(productDetail);

        return new ResponseEntity<>(productDetailDto, HttpStatus.OK);
    }

    @PostMapping("/update/content")
    public ResponseEntity<?> updateContent(@RequestBody ProductDetailDto productDetailDto,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        ProductDetail productDetail = productDetailService.retrieveById(productDetailDto.getId());
        productDetailService.updateProductDetailContent(productDetail, productDetailDto);

        return ResponseEntity.ok().build();
    }
}
