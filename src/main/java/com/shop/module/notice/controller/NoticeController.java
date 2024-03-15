package com.shop.module.notice.controller;

import com.shop.common.exception.UnauthorizedException;
import com.shop.module.notice.dto.NoticeListSearchRequest;
import com.shop.module.notice.dto.NoticeDto;
import com.shop.module.notice.dto.NoticeListResponse;
import com.shop.module.notice.entity.Notice;
import com.shop.module.notice.entity.NoticeImageType;
import com.shop.module.notice.entity.NoticeType;
import com.shop.module.notice.service.NoticeImageService;
import com.shop.module.notice.service.NoticeService;
import com.shop.module.user.service.JwtService;
import com.shop.common.util.JsonUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notice")
public class NoticeController {

    private final JwtService jwtService;
    private final NoticeService noticeService;
    private final NoticeImageService noticeImageService;

    /**
     * 공지 단일 취득
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getNotice(@PathVariable("id") Long noticeId) {

        Notice notice = noticeService.retrieveById(noticeId);
        NoticeDto noticeDto = noticeService.getDto(notice);

        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    /**
     * 특정 타입의 공지 사항 취득
     */
    @GetMapping("/active-items/{type}")
    public ResponseEntity<?> getActiveNoticesByType(@PathVariable("type") String type) {
        List<Notice> noticeList;

        try {
            // 문자열 type을 대문자로 변환하고, 이를 NoticeType 열거형으로 변환합니다.
            // 만약 type이 NoticeType의 유효한 값이 아니면, IllegalArgumentException이 발생합니다.
            NoticeType noticeType = NoticeType.valueOf(type.toUpperCase());
            // 유효한 NoticeType 값이 제공되면, 해당 타입의 공지 목록을 검색합니다.
            noticeList = noticeService.retrieveActiveNoticeListWithMainImageByType(noticeType);

        } catch (IllegalArgumentException e) {
            // 만약 주어진 type이 유효하지 않은 경우, 모든 공지 목록을 검색합니다.
            noticeList = noticeService.retrieveActiveNoticeListWithMainImage();
        }

        List<NoticeDto> noticeDtoList = noticeService.getDtoListWithMainImage(noticeList);

        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    /**
     * 모달 표시 사항 취득
     */
    @GetMapping("/show-in-modal")
    public ResponseEntity<?> getModalNotices() {

        List<Notice> noticeList = noticeService.retrieveActiveNoticeListWithModalImage();
        List<NoticeDto> noticeDtoList = noticeService.getDtoListWithModalImage(noticeList);

        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    /**
     * 슬라이더 표시 사항 취득
     */
    @GetMapping("/show-in-slider")
    public ResponseEntity<?> getSliderNotices() {

        List<Notice> noticeList = noticeService.retrieveActiveNoticeListWithSliderImage();
        List<NoticeDto> noticeDtoList = noticeService.getDtoListWithSliderImage(noticeList);

        return new ResponseEntity<>(noticeDtoList, HttpStatus.OK);
    }

    /**
     * 상품 전체 취득
     */
    @GetMapping("/list")
    public ResponseEntity getNoticeList(@CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res,
                                        Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {

            Page<Notice> noticePage = noticeService.retrieveAllPaginated(pageable);
            List<NoticeDto> noticeDtoList = noticeService.getDtoListWithImages(noticePage.getContent());

            NoticeListResponse noticeListResponse = NoticeListResponse.builder()
                    .notices(noticeDtoList)
                    .totalPages(noticePage.getTotalPages())
                    .build();

            return new ResponseEntity<>(noticeListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 공지 단일 취득
     */
    @GetMapping("/detail/{id}")
    public ResponseEntity getNoticeDetail(@PathVariable("id") Long noticeId,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Notice notice = noticeService.retrieveById(noticeId);
        NoticeDto noticeDto = noticeService.getDtoWithImages(notice);

        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    /**
     * 공지 내용 취득
     */
    @GetMapping("/content/{id}")
    public ResponseEntity getNoticeContent(@PathVariable("id") Long noticeId,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }
        Notice notice = noticeService.retrieveById(noticeId);
        NoticeDto noticeDto = noticeService.convertToDto(notice);

        return new ResponseEntity<>(noticeDto, HttpStatus.OK);
    }

    /**
     * 조건으로 공지 검색
     */
    @PostMapping("/search")
    public ResponseEntity searchNoticeList(@RequestBody NoticeListSearchRequest noticeListSearchRequest,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res,
                                           Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Notice> noticePage = noticeService.searchAllPaginated(noticeListSearchRequest, pageable);
            List<NoticeDto> noticeDtoList = noticeService.getDtoListWithImages(noticePage.getContent());

            NoticeListResponse noticeListResponse = NoticeListResponse.builder()
                    .notices(noticeDtoList)
                    .totalPages(noticePage.getTotalPages())
                    .build();

            return new ResponseEntity<>(noticeListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerNotice(@RequestParam("notice") String noticeJson,
                                            @RequestPart(value = "mainImage", required = false) MultipartFile mainImageFile,
                                            @RequestPart(value = "sliderImage", required = false) MultipartFile sliderImageFile,
                                            @RequestPart(value = "modalImage", required = false) MultipartFile modalImageFile,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) { // 토큰이 유효하지 않은 경우
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);

        NoticeDto noticeDto = Optional.ofNullable(JsonUtils.fromJson(noticeJson, NoticeDto.class))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse input data"));

        // 입력 데이터 체크
        noticeService.checkInput(noticeDto);

        boolean isMainImage = (mainImageFile != null) ? true : false;
        boolean isSliderImage = (sliderImageFile != null) ? true : false;
        boolean isModalImage = (modalImageFile != null) ? true : false;

        // 입력 데이터 Notice 테이블에 데이터 삽입
        Notice notice = noticeService.insertNotice(noticeDto, isSliderImage, isModalImage);

        // 입력 데이터 NoticeImage 테이블에 데이터 삽입
        if (isMainImage) {
            noticeImageService.saveNoticeImage(userId, notice, NoticeImageType.MAIN, mainImageFile);
        }
        if (isSliderImage) {
            noticeImageService.saveNoticeImage(userId, notice, NoticeImageType.SLIDER, sliderImageFile);
        }
        if (isModalImage) {
            noticeImageService.saveNoticeImage(userId, notice, NoticeImageType.MODAL, modalImageFile);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/content")
    public ResponseEntity<?> updateNoticeContent(@RequestBody NoticeDto noticeDto,
                                                 @CookieValue(value = "token", required = false) String accessToken,
                                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                 HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Notice notice = noticeService.retrieveById(noticeDto.getId());
        noticeService.updateNoticeContent(notice, noticeDto);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update/single")
    public ResponseEntity<?> updateNotice(@RequestParam("notice") String noticeJson,
                                          @RequestPart(value = "mainImage", required = false) MultipartFile mainImageFile,
                                          @RequestPart(value = "sliderImage", required = false) MultipartFile sliderImageFile,
                                          @RequestPart(value = "modalImage", required = false) MultipartFile modalImageFile,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) { // 토큰이 유효하지 않은 경우
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        try {
            Long userId = jwtService.getId(token);

            NoticeDto noticeDto = Optional.ofNullable(JsonUtils.fromJson(noticeJson, NoticeDto.class))
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to parse input data"));

            Notice notice = noticeService.retrieveById(noticeDto.getId());

            noticeService.checkInput(noticeDto);

            boolean isMainImage = (mainImageFile != null) ? true : false;
            boolean isSliderImage = (sliderImageFile != null) ? true : false;
            boolean isModalImage = (modalImageFile != null) ? true : false;

            // 입력 데이터 NoticeImage 테이블에 데이터 삽입
            if (isMainImage) {
                noticeImageService.updateNoticeImage(userId, notice, NoticeImageType.MAIN, mainImageFile);
            }
            if (isSliderImage) {
                noticeImageService.updateNoticeImage(userId, notice, NoticeImageType.SLIDER, sliderImageFile);
            }
            if (isModalImage) {
                noticeImageService.updateNoticeImage(userId, notice, NoticeImageType.MODAL, modalImageFile);
            }

            noticeService.updateNotice(notice, noticeDto);

            return ResponseEntity.ok().build();

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating notice.");
        }
    }

    @PostMapping("/update/multiple")
    public ResponseEntity updateNotices(@RequestBody List<NoticeDto> noticeDtoList,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        noticeService.updateNotices(noticeDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
