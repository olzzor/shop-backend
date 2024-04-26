package com.shop.module.wishlist.controller;

import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.wishlist.service.WishlistService;
import com.shop.module.wishlist.dto.WishlistInfo;
import com.shop.module.wishlist.dto.WishlistDto;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.user.entity.User;
import com.shop.module.product.service.ProductSizeService;
import com.shop.module.user.service.JwtService;
import com.shop.module.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {

    private final JwtService jwtService;
    private final UserService userService;
    private final WishlistService wishlistService;
    private final ProductSizeService productSizeService;

    /**
     * 위시리스트 체크
     */
    @GetMapping("/check/{productSizeId}")
    public ResponseEntity<?> checkWishlist(@PathVariable("productSizeId") Long productSizeId,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            WishlistInfo fcRes = wishlistService.checkWishlist(jwtService.getId(token), productSizeId);
            return new ResponseEntity<>(fcRes, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 위시리스트 정보 취득
     */
    @GetMapping("/get")
    public ResponseEntity getWishlists(@CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            // 유저 아이디로 위시리스트 정보 취득
            List<Wishlist> wishlistList = wishlistService.getWishlistList(jwtService.getId(token));
            List<WishlistDto> wishlistDtoList = wishlistService.getWishlistDtoList(wishlistList);

            return new ResponseEntity<>(wishlistDtoList, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 위시리스트 추가
     */
    @PostMapping("/add/{productSizeId}")
    public ResponseEntity addToWishlist(@PathVariable("productSizeId") Long productSizeId,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            User user = userService.retrieveById(jwtService.getId(token));
            ProductSize productSize = productSizeService.retrieveById(productSizeId);

            wishlistService.addToWishlist(user, productSize);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 위시리스트 단일 삭제
     */
    @DeleteMapping("/delete/{wishlistId}")
    public ResponseEntity deleteFromWishlist(@PathVariable("wishlistId") Long wishlistId,
                                             @CookieValue(value = "token", required = false) String accessToken,
                                             @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                             HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            wishlistService.deleteById(wishlistId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 위시리스트 전체 삭제
     */
    @DeleteMapping("/delete/all")
    public ResponseEntity deleteUserWishlist(@CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            wishlistService.deleteAllByUserId(jwtService.getId(token));
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
