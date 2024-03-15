package com.shop.module.recentlyviewedproduct.controller;

import com.shop.common.exception.UnauthorizedException;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.entity.Product;
import com.shop.module.product.service.ProductService;
import com.shop.module.recentlyviewedproduct.dto.RecentlyViewedProductDto;
import com.shop.module.recentlyviewedproduct.dto.RecentlyViewedProductListResponse;
import com.shop.module.recentlyviewedproduct.dto.RecentlyViewedProductRequest;
import com.shop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.shop.module.recentlyviewedproduct.service.RecentlyViewedProductService;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recently-viewed-product")
public class RecentlyViewedProductController {

    private final JwtService jwtService;
    private final ProductService productService;
    private final RecentlyViewedProductService recentlyViewedProductService;

    /**
     * 유저의 상품 조회 기록을 처리하는 엔드포인트.
     *
     * 유저가 상품을 조회할 때마다 이 엔드포인트가 호출되어 해당 행위를 기록
     * 유효한 상품인지 확인하고, 유저의 최근 조회한 상품 기록을 특정 개수 (예: 10개)로 유지
     * 필요에 따라 오래된 조회 기록은 삭제
     */
    @PostMapping("/record/{productId}")
    public ResponseEntity recordView(@PathVariable("productId") Long productId,
                                     @CookieValue(value = "token", required = false) String accessToken,
                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse res) {

        // 인증 토큰 조회
        String token = jwtService.getToken(accessToken, refreshToken, res);

        // 토큰이 유효한지 확인
        if (token != null) {

            // 상품이 유효하지 않은 경우, NOT_FOUND 에러 반환
            if (!productService.existProduct(productId)) {
                return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
            }

            // 유저의 상품 조회 기록을 작성
            recentlyViewedProductService.recordRecentlyViewedProduct(jwtService.getId(token), productId);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            // 토큰이 유효하지 않은 경우, UNAUTHORIZED 에러 반환
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     *
     */
    @GetMapping("/get-by-database")
    public ResponseEntity getByDataBase(@CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res,
                                        Pageable pageable) {

        // 인증 토큰 조회
        String token = jwtService.getToken(accessToken, refreshToken, res);

        // 토큰이 유효한지 확인
        if (token != null) {

            Long userId = jwtService.getId(token);

            Page<RecentlyViewedProduct> rvpPage = recentlyViewedProductService.getRecentlyViewedProductPage(userId, pageable);
            List<RecentlyViewedProductDto> rvpDtoList = recentlyViewedProductService.getRecentlyViewedProductDtoList(rvpPage.getContent());

            RecentlyViewedProductListResponse rvpListRes = RecentlyViewedProductListResponse.builder()
                    .recentlyViewedProducts(rvpDtoList)
                    .totalPages(rvpPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(rvpListRes, HttpStatus.OK);

        } else {
            // 토큰이 유효하지 않은 경우, UNAUTHORIZED 에러 반환
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/get-by-localstorage")
    public ResponseEntity getByLocalStorage(@RequestBody List<RecentlyViewedProductRequest> recentlyViewedProductRequestList) {

        // 상품 아이디 목록 추출
        List<Long> productIds = recentlyViewedProductRequestList.stream()
                .map(rvpReqList -> rvpReqList.getProductId()).collect(Collectors.toList());

        // 상품 아이디 목록을 사용하여 상품 정보 취득
        List<Product> productList = productService.retrieveAllByIds(productIds);
        List<ProductDto> productDtoList = productService.getDtoListWithMainImage(productList);

        // 클라이언트에서 전송한 데이터와 상품 정보를 사용하여 RecentlyViewedProductDto 리스트 구성
        List<RecentlyViewedProductDto> rvpDtoList = new ArrayList<>();

        // 클라이언트에서 전송한 viewedAt 정보와 상품 정보 조합
        for (RecentlyViewedProductRequest rvpRequest : recentlyViewedProductRequestList) {
            RecentlyViewedProductDto rvpDto = new RecentlyViewedProductDto();

            for (ProductDto productDto : productDtoList) {
                if (rvpRequest.getProductId().equals(productDto.getId())) {
                    rvpDto.setProduct(productDto);
                    rvpDto.setViewedAt(LocalDateTime.parse(rvpRequest.getViewedAt()));
                    rvpDtoList.add(rvpDto);
                    break;
                }
            }
        }

        // viewedAt 정보를 기반으로 내림차순 정렬
        rvpDtoList.sort((rvp1, rvp2) -> rvp2.getViewedAt().compareTo(rvp1.getViewedAt()));

        return new ResponseEntity<>(rvpDtoList, HttpStatus.OK);
    }

    /**
     * 로컬 스토리지의 recentlyViewedProducts 데이터를 데이터베이스에 동기화하는 API
     */
    @PostMapping("/sync")
    public ResponseEntity syncRecentlyViewedProducts(@RequestBody List<RecentlyViewedProductRequest> recentlyViewedProductRequestList,
                                                     @CookieValue(value = "token", required = false) String accessToken,
                                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                     HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res); // 인증 토큰 조회

        if (token == null) { // 토큰이 유효하지 않은 경우
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        for (RecentlyViewedProductRequest rvpReq : recentlyViewedProductRequestList) {
            // 유저의 상품 조회 기록을 작성
            recentlyViewedProductService.syncRecentlyViewedProduct(jwtService.getId(token), rvpReq);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
