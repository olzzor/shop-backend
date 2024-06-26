package com.shop.module.review.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.shop.common.exception.UnauthorizedException;
import com.shop.common.util.JsonUtils;
import com.shop.common.util.StringUtils;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.product.service.ProductService;
import com.shop.module.product.service.ProductSizeService;
import com.shop.module.review.dto.*;
import com.shop.module.review.entity.Review;
import com.shop.module.review.entity.ReviewImage;
import com.shop.module.review.service.ReviewImageService;
import com.shop.module.review.service.ReviewService;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final JwtService jwtService;
    private final ReviewService reviewService;
    private final ReviewImageService reviewImageService;
    private final ProductService productService;
    private final ProductSizeService productSizeService;

    /**
     * 일반 사용자를 위한 단일 리뷰 취득 (토큰 검증 불필요)
     *
     * @param reviewId  리뷰 식별자
     * @return 리뷰 상세 정보
     */
    @GetMapping("/public/{reviewId}")
    public ResponseEntity getPublicReviewInfo(@PathVariable("reviewId") Long reviewId) {

        Review review = reviewService.retrieveById(reviewId);
        ReviewDto reviewDto = getDtoIncludeImages(review);

        return new ResponseEntity<>(reviewDto, HttpStatus.OK);
    }

    /**
     * 리뷰 작성자를 위한 단일 리뷰 취득 (토큰 검증 필요)
     *
     * @param reviewId      리뷰 식별자
     * @param accessToken   접근 토큰
     * @param refreshToken  새로고침 토큰
     * @param res           응답 객체
     * @return 리뷰 상세 정보
     */
    @GetMapping("/private/{reviewId}")
    public ResponseEntity getPrivateReviewInfo(@PathVariable("reviewId") Long reviewId,
                                               @CookieValue(value = "token", required = false) String accessToken,
                                               @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                               HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Review review = reviewService.retrieveById(reviewId);
            ReviewDto reviewDto = getDtoIncludeImages(review);

            return new ResponseEntity<>(reviewDto, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 단일 리뷰 취득
     */
    @GetMapping("/detail/admin/{reviewId}")
    public ResponseEntity getReviewDetail(@PathVariable("reviewId") Long reviewId,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Review review = reviewService.retrieveById(reviewId);
            ReviewDto reviewDto = reviewService.getDtoWithUserAndImages(review);

            return new ResponseEntity<>(reviewDto, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/list")
    public ResponseEntity getReviewList(@CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res,
                                        Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Review> reviewPage = reviewService.retrieveAllPaged(pageable);
            List<ReviewDto> reviewDtoList = reviewService.getDtoListWithUserEmail(reviewPage.getContent());

            ReviewListResponse reviewListResponse = ReviewListResponse.builder()
                    .reviews(reviewDtoList)
                    .totalPages(reviewPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(reviewListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 전체 리뷰 취득
     */
    @GetMapping("/all")
    public ResponseEntity getReviewAll(Pageable pageable) {

        Page<Review> reviewPage = reviewService.retrieveAllActivated(pageable);
        List<Review> reviewList = reviewPage.getContent();

        double averageRating = reviewList.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        // 0.1 단위로 반올림
        averageRating = Math.round(averageRating * 10) / 10.0;

        // 각 점수별 리뷰 수를 계산
        Map<Byte, Long> countRating = reviewList.stream()
                .collect(Collectors.groupingBy(Review::getRating, Collectors.counting()));

        List<ReviewDto> reviewDtoList = reviewList.stream()
                .map(this::getDtoIncludeImagesAndProducts)
                .collect(Collectors.toList());

        ReviewOverviewResponse roRes = ReviewOverviewResponse.builder()
                .reviews(reviewDtoList)
                .totalPages(reviewPage.getTotalPages())
                .totalReviews(reviewPage.getTotalElements())
                .averageRating(averageRating)
                .countRating(countRating)
                .build();

        return new ResponseEntity<>(roRes, HttpStatus.OK);
    }

    @PostMapping("/write")
    public ResponseEntity<?> writeReview(@RequestParam("review") String noticeJson,
                                         @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) { // 토큰이 유효하지 않은 경우
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);

        ReviewWriteRequest rwReq = Optional.ofNullable(JsonUtils.fromJson(noticeJson, ReviewWriteRequest.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력 데이터 파싱에 실패했습니다."));

        reviewService.checkInput(rwReq);

        // 입력 데이터 Review 테이블에 데이터 삽입
        Review review = reviewService.insertReview(userId, rwReq);

        // 입력 데이터 ReviewImage 테이블에 데이터 삽입
        if (files != null && !files.isEmpty()) {
            reviewImageService.saveReviewImages(userId, review, files);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editReview(@RequestParam("review") String noticeJson,
                                        @RequestParam("existingImages") String existingImagesJson,
                                        @RequestPart(value = "files", required = false) List<MultipartFile> files,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) { // 토큰이 유효하지 않은 경우
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);

        ReviewEditRequest reReq = Optional.ofNullable(JsonUtils.fromJson(noticeJson, ReviewEditRequest.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력 데이터 파싱에 실패했습니다."));

        List<ReviewImageDto> existingImages = Optional.ofNullable(JsonUtils.fromJson(existingImagesJson, new TypeReference<List<ReviewImageDto>>() {
                }))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "입력 이미지 데이터 파싱에 실패했습니다."));

        // 데이터 입력 체크
        reviewService.checkInput(reReq);

        // 입력 데이터 Review 테이블에 데이터 갱신
        Review review = reviewService.updateReview(reReq);

        // 현재 ReviewImage 리스트 취득
        List<ReviewImage> currentImages = reviewImageService.retrieveAllByReviewId(review.getId());

        // 기존 이미지 정보를 바탕으로 ReviewImage 테이블 데이터 삭제
        reviewImageService.deleteReviewImages(currentImages, existingImages);

        // 기존 이미지 정보를 바탕으로 ReviewImage 테이블 데이터 갱신
        reviewImageService.updateReviewImages(currentImages, existingImages);

        // 새로운 이미지 파일이 있다면, ReviewImage 테이블에 데이터 삽입
        if (files != null && !files.isEmpty()) {
            reviewImageService.addReviewImages(userId, review, files, existingImages);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchReviewList(@RequestBody ReviewListSearchRequest reviewListSearchRequest,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res,
                                           Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Review> reviewPage = reviewService.searchAllPaginated(reviewListSearchRequest, pageable);
            List<ReviewDto> reviewDtoList = reviewService.getDtoListWithUserEmail(reviewPage.getContent());

            ReviewListResponse reviewListResponse = ReviewListResponse.builder()
                    .reviews(reviewDtoList)
                    .totalPages(reviewPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(reviewListResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/update/single")
    public ResponseEntity<?> updateReview(@RequestBody ReviewDto reviewDto,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {
        String token = jwtService.getToken(accessToken, refreshToken, res);
        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        // 리뷰 DB 갱신
        Review review = reviewService.retrieveById(reviewDto.getId());
        reviewService.updateReviewByAdmin(review, reviewDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/multiple")
    public ResponseEntity<?> updateReviews(@RequestBody List<ReviewDto> reviewDtoList,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);
        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        reviewService.updateReviewsByAdmin(reviewDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ReviewDto getDtoIncludeImages(Review review) {
        List<ReviewImage> reviewImageList = review.getReviewImages();
        List<ReviewImageDto> reviewImageDtoList = reviewImageService.convertToDtoList(reviewImageList);

        ReviewDto reviewDto = reviewService.convertToDto(review);
        reviewDto.setReviewImages(reviewImageDtoList);
        reviewDto.setUserEmail(StringUtils.obfuscatedEmail(review.getUser().getEmail()));

        return reviewDto;
    }

    public ReviewDto getDtoIncludeImagesAndProducts(Review review) {

        List<ReviewImage> reviewImageList = review.getReviewImages();
        List<ReviewImageDto> reviewImageDtoList = reviewImageService.convertToDtoList(reviewImageList);

        List<ProductSizeDto> productSizeDtoList = new ArrayList<>();
        ProductSize productSize = review.getOrderDetail().getProductSize();
        ProductDto productDto = productService.getDtoWithMainImage(productSize.getProduct());
        ProductSizeDto productSizeDto = productSizeService.convertToDto(productSize);
        productSizeDto.setProduct(productDto);
        productSizeDtoList.add(productSizeDto);

        ReviewDto reviewDto = reviewService.convertToDto(review);
        reviewDto.setUserEmail(StringUtils.obfuscatedEmail(review.getUser().getEmail()));
        reviewDto.setReviewImages(reviewImageDtoList);
        reviewDto.setProductSizes(productSizeDtoList);

        return reviewDto;
    }
}
