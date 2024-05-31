package com.shop.module.recommendedproduct.controller;

import com.shop.common.exception.UnauthorizedException;
import com.shop.module.recommendedproduct.dto.RecommendedProductDto;
import com.shop.module.recommendedproduct.dto.RecommendedProductListResponse;
import com.shop.module.recommendedproduct.dto.RecommendedProductUpsertRequest;
import com.shop.module.recommendedproduct.entity.RecommendedProduct;
import com.shop.module.recommendedproduct.service.RecommendedProductService;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommended-product")
public class RecommendedProductController {

    private final JwtService jwtService;
    private final RecommendedProductService recommendedProductService;

    @GetMapping("/get")
    public ResponseEntity<?> getForUser(Pageable pageable) {
        Page<RecommendedProduct> rpPage = recommendedProductService.retrieveAllPaged(pageable);
        List<RecommendedProductDto> rpDtoList = recommendedProductService.getDtoList(rpPage.getContent()); // TODO null 대응

        RecommendedProductListResponse rpListRes = RecommendedProductListResponse.builder()
                .recommendedProducts(rpDtoList)
                .totalPages(rpPage.getTotalPages())
                .build();

        return new ResponseEntity<>(rpListRes, HttpStatus.OK);
    }

    /**
     * 상품 전체 취득
     */
    @GetMapping("/list")
    public ResponseEntity getForAdmin(@CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        List<RecommendedProduct> rpList = recommendedProductService.retrieveAll();
        List<RecommendedProductDto> rpDtoList = recommendedProductService.getDtoListDetail(rpList);

        return new ResponseEntity<>(rpDtoList, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<?> insertRecommendedProduct(@RequestBody List<RecommendedProductUpsertRequest> rpUpsReqList,
                                                      @CookieValue(value = "token", required = false) String accessToken,
                                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        // 추천 상품 DB 작성
        recommendedProductService.insertRecommendedProducts(rpUpsReqList);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateRecommendedProducts(@RequestBody List<RecommendedProductUpsertRequest> rpUpsReqList,
                                                       @CookieValue(value = "token", required = false) String accessToken,
                                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        // 추천 상품 DB 갱신
        recommendedProductService.updateRecommendedProducts(rpUpsReqList);
        return ResponseEntity.ok().build();
    }

    /**
     * 추천 상품 삭제
     */
    @DeleteMapping("/delete/{recommendedProductId}")
    public ResponseEntity delete(@PathVariable("recommendedProductId") Long recommendedProductId,
                                 @CookieValue(value = "token", required = false) String accessToken,
                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                 HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        recommendedProductService.deleteById(recommendedProductId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
