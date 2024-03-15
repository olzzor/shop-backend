package com.shop.module.favorite.controller;

import com.shop.module.favorite.service.FavoriteService;
import com.shop.module.favorite.dto.FavoriteInfo;
import com.shop.module.favorite.dto.FavoriteDto;
import com.shop.module.favorite.entity.Favorite;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.user.entity.User;
import com.shop.module.product.service.ProductService;
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
@RequestMapping("/api/favorite")
public class FavoriteController {

    private final JwtService jwtService;
    private final UserService userService;
    private final FavoriteService favoriteService;
    private final ProductService productService;
    private final ProductSizeService productSizeService;

    /**
     * 관심 상품 체크
     */
    @GetMapping("/check/{productSizeId}")
    public ResponseEntity<?> checkFavorite(@PathVariable("productSizeId") Long productSizeId,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            FavoriteInfo fcRes = favoriteService.checkFavorite(jwtService.getId(token), productSizeId);
            return new ResponseEntity<>(fcRes, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 관심 상품 취득
     */
    @GetMapping("/get")
    public ResponseEntity getFavorites(@CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            // 유저 아이디로 관심 상품 정보 취득
            List<Favorite> favoriteList = favoriteService.getFavoriteList(jwtService.getId(token));
            List<FavoriteDto> favoriteDtoList = favoriteService.getFavoriteDtoList(favoriteList);

            return new ResponseEntity<>(favoriteDtoList, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 관심 상품 추가
     */
    @PostMapping("/add/{productSizeId}")
    public ResponseEntity addFavorite(@PathVariable("productSizeId") Long productSizeId,
                                      @CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            User user = userService.retrieveById(jwtService.getId(token));
            ProductSize productSize = productSizeService.retrieveById(productSizeId);

            favoriteService.addFavorite(user, productSize);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 관심 상품 단일 삭제
     */
    @DeleteMapping("/delete/{favoriteId}")
    public ResponseEntity deleteFavorite(@PathVariable("favoriteId") Long favoriteId,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            favoriteService.deleteById(favoriteId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 관심 상품 전체 삭제
     */
    @DeleteMapping("/delete/all")
    public ResponseEntity deleteFavoriteAll(@CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            favoriteService.deleteAllByUserId(jwtService.getId(token));
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
