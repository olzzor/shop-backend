package com.bridgeshop.module.coupon.controller;

import com.bridgeshop.module.coupon.dto.*;
import com.bridgeshop.module.coupon.service.CouponCategoryService;
import com.bridgeshop.module.coupon.service.CouponProductService;
import com.bridgeshop.module.coupon.service.CouponService;
import com.bridgeshop.module.coupon.service.CouponUserService;
import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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

        if (token != null) {
            Coupon coupon = couponService.retrieveById(id);
            CouponDto couponDto = couponService.getCouponDtoDetail(coupon);

            return new ResponseEntity<>(couponDto, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/list")
    public ResponseEntity getCouponList(@CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res,
                                        Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Coupon> couponPage = couponService.retrieveAllPaged(pageable);
            List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponPage.getContent());

            CouponListResponse couponListResponse = CouponListResponse.builder()
                    .coupons(couponDtoList)
                    .totalPages(couponPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(couponListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/list/available/user")
    public ResponseEntity getAvailableCouponListForUser(@CookieValue(value = "token", required = false) String accessToken,
                                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            List<Coupon> couponList = couponService.retrieveAllForUser(jwtService.getId(token));
            List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponList);

            return new ResponseEntity<>(couponDtoList, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/list/available")
    public ResponseEntity getAvailableCouponList(@RequestBody List<CouponEligibilityRequest> requestList,
                                                 @CookieValue(value = "token", required = false) String accessToken,
                                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                 HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {

            Long userId = jwtService.getId(token);

            List<CouponListAvailableResponse> responseList = new ArrayList<>();

            for (CouponEligibilityRequest request : requestList) {

                List<Coupon> couponList = couponService.getCouponListAvailable(userId, request);
                List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponList);

                CouponListAvailableResponse response = new CouponListAvailableResponse();
                response.setCartProductId(request.getCartProductId());
                response.setCoupons(couponDtoList);

                responseList.add(response);
            }

            return new ResponseEntity<>(responseList, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/search")
    public ResponseEntity searchCouponList(@RequestBody CouponListSearchRequest couponListSearchRequest,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res,
                                           Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Coupon> couponPage = couponService.searchAllPaginated(couponListSearchRequest, pageable);
            List<CouponDto> couponDtoList = couponService.getCouponDtoListDetail(couponPage.getContent());

            CouponListResponse couponListResponse = CouponListResponse.builder()
                    .coupons(couponDtoList)
                    .totalPages(couponPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(couponListResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/regist")
    public ResponseEntity<?> registerCoupon(@RequestBody CouponUpdateRequest couponUpdateRequest,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            couponService.checkInput(couponUpdateRequest);
            Coupon coupon = couponService.insertCoupon(couponUpdateRequest);

            List<Long> userIds = couponUpdateRequest.getUsers();
            couponUserService.insertCouponUser(coupon, userIds);

            List<Long> productIds = couponUpdateRequest.getProducts();
            couponProductService.insertCouponProduct(coupon, productIds);

            List<Long> categoryIds = couponUpdateRequest.getCategories();
            couponCategoryService.insertCouponCategory(coupon, categoryIds);

            return new ResponseEntity<>(HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("인증되지 않았습니다. 다시 로그인해주세요.");
        }
    }

    @PostMapping("/update/single")
    public ResponseEntity updateCoupon(@RequestBody CouponUpdateRequest couponUpdateRequest,
                                       @CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            couponService.checkInput(couponUpdateRequest);
            Coupon coupon = couponService.updateCoupon(couponUpdateRequest);

            List<Long> userIds = couponUpdateRequest.getUsers();
            couponUserService.updateCouponUser(coupon, userIds);

            List<Long> productIds = couponUpdateRequest.getProducts();
            couponProductService.updateCouponProduct(coupon, productIds);

            List<Long> categoryIds = couponUpdateRequest.getCategories();
            couponCategoryService.updateCouponCategory(coupon, categoryIds);

            return new ResponseEntity<>(HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("인증되지 않았습니다. 다시 로그인해주세요.");
        }
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
