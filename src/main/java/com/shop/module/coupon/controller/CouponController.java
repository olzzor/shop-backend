package com.shop.module.coupon.controller;

import com.shop.common.exception.UnauthorizedException;
import com.shop.module.coupon.dto.*;
import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.service.CouponCategoryService;
import com.shop.module.coupon.service.CouponProductService;
import com.shop.module.coupon.service.CouponService;
import com.shop.module.coupon.service.CouponUserService;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupon")
public class CouponController {

    private final JwtService jwtService;
    private final CouponService couponService;
    private final CouponUserService couponUserService;
    private final CouponProductService couponProductService;
    private final CouponCategoryService couponCategoryService;

    @GetMapping("/detail/{id}")
    public ResponseEntity getCouponDetail(@PathVariable("id") Long id,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Coupon coupon = couponService.retrieveById(id);
        CouponDto couponDto = couponService.getCouponDtoDetail(coupon);

        return new ResponseEntity<>(couponDto, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity getCouponList(@CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res,
                                        Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Page<Coupon> couponPage = couponService.retrieveAllPaged(pageable);
        List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponPage.getContent());

        CouponListResponse couponListResponse = CouponListResponse.builder()
                .coupons(couponDtoList)
                .totalPages(couponPage.getTotalPages())
                .build();

        return new ResponseEntity<>(couponListResponse, HttpStatus.OK);
    }

    @GetMapping("/list/available/user")
    public ResponseEntity getAvailableCouponListForUser(@CookieValue(value = "token", required = false) String accessToken,
                                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        List<Coupon> couponList = couponService.retrieveAllForUser(jwtService.getId(token));
        List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponList);

        return new ResponseEntity<>(couponDtoList, HttpStatus.OK);
    }

    @PostMapping("/list/available")
    public ResponseEntity getAvailableCouponList(@RequestBody List<CouponEligibilityRequest> requestList,
                                                 @CookieValue(value = "token", required = false) String accessToken,
                                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                 HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);

        List<CouponListAvailableResponse> responseList = new ArrayList<>();

        for (CouponEligibilityRequest request : requestList) {

            List<Coupon> couponList = couponService.getAvailableCouponList(userId, request);
            List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponList);

            CouponListAvailableResponse response = CouponListAvailableResponse.builder()
                    .cartProductId(request.getCartProductId())
                    .coupons(couponDtoList)
                    .build();

            responseList.add(response);
        }

        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

    @PostMapping("/get-available-list")
    public ResponseEntity getAvailableCouponList(@CookieValue(value = "token", required = false) String accessToken,
                                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                 HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);

        List<Coupon> couponList = couponService.getAvailableCouponListForCart(userId);
        List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponList);

        return new ResponseEntity<>(couponDtoList, HttpStatus.OK);
    }

    @PostMapping("/get-user-select")
    public ResponseEntity getUserSelectCoupon(@CookieValue(value = "token", required = false) String accessToken,
                                              @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                              HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);
        Coupon coupon = couponService.getSelectedCouponForUser(userId);

        return new ResponseEntity<>(coupon == null ? 0L : coupon.getId(), HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchCouponList(@RequestBody CouponListSearchRequest couponListSearchRequest,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res,
                                           Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Page<Coupon> couponPage = couponService.searchAllPaginated(couponListSearchRequest, pageable);
        List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponPage.getContent());

        CouponListResponse couponListResponse = CouponListResponse.builder()
                .coupons(couponDtoList)
                .totalPages(couponPage.getTotalPages())
                .build();

        return new ResponseEntity<>(couponListResponse, HttpStatus.OK);
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registerCoupon(@RequestBody CouponUpdateRequest couponUpdateRequest,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        couponService.checkInput(couponUpdateRequest);
        Coupon coupon = couponService.insertCoupon(couponUpdateRequest);

        List<Long> userIds = couponUpdateRequest.getUsers();
        couponUserService.insertCouponUser(coupon, userIds);

        List<Long> productIds = couponUpdateRequest.getProducts();
        couponProductService.insertCouponProduct(coupon, productIds);

        List<String> categoryCodes = couponUpdateRequest.getCategories();
        couponCategoryService.insertCouponCategory(coupon, categoryCodes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/single")
    public ResponseEntity updateCoupon(@RequestBody CouponUpdateRequest couponUpdateRequest,
                                       @CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        couponService.checkInput(couponUpdateRequest);
        Coupon coupon = couponService.updateCoupon(couponUpdateRequest);

        List<Long> userIds = couponUpdateRequest.getUsers();
        couponUserService.updateCouponUser(coupon, userIds);

        List<Long> productIds = couponUpdateRequest.getProducts();
        couponProductService.updateCouponProduct(coupon, productIds);

        List<String> categoryCodes = couponUpdateRequest.getCategories();
        couponCategoryService.updateCouponCategory(coupon, categoryCodes);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/multiple")
    public ResponseEntity updateCoupons(@RequestBody List<CouponDto> couponDtoList,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        couponService.updateCoupons(couponDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
