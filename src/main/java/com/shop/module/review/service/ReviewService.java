package com.shop.module.review.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.module.order.entity.Order;
import com.shop.module.order.service.OrderService;
import com.shop.module.review.dto.*;
import com.shop.module.review.entity.Review;
import com.shop.module.review.entity.ReviewImage;
import com.shop.module.review.mapper.ReviewMapper;
import com.shop.module.review.repository.ReviewRepository;
import com.shop.module.user.dto.UserDto;
import com.shop.module.user.entity.User;
import com.shop.module.user.mapper.UserMapper;
import com.shop.module.user.service.UserService;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewImageService reviewImageService;
    private final UserService userService;
    private final OrderService orderService;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final UserMapper userMapper;

    private static final int MAX_TITLE_LENGTH = 100;
    private static final int MAX_CONTENT_LENGTH = 1000;

//    @Value("${upload.directory.review}")
//    private String uploadDir;

    public boolean isExistingById(Long id) {
        return reviewRepository.existsById(id);
    }

    public Review retrieveById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Review not found"));
    }

    public Page<Review> retrieveAllActivated(Pageable pageable) {
        return reviewRepository.findAllByActivateFlag(true, pageable);
    }

    public Page<Review> retrieveAllPaged(Pageable pageable) {
        return reviewRepository.findAll(pageable);
    }

    public ReviewDto convertToDto(Review review) {
        return reviewMapper.mapToDto(review);
    }

    public List<ReviewDto> convertToDtoList(List<Review> reviewList) {
        return reviewMapper.mapToDtoList(reviewList);
    }

    public List<ReviewDto> getDtoListWithUserEmail(List<Review> reviewList) {
        return reviewList.stream()
                .map(review -> {
                    ReviewDto reviewDto = reviewMapper.mapToDto(review); // Review를 ReviewDto로 변환
                    String userEmail = review.getUser() != null ? review.getUser().getEmail() : null; // Review 작성 User의 이메일을 가져옴
                    reviewDto.setUserEmail(userEmail); // ReviewDto에 userEmail을 설정
                    return reviewDto;
                })
                .collect(Collectors.toList());
    }

    public ReviewDto getDtoWithUserAndImages(Review review) {
        // Review를 ReviewDto로 변환
        ReviewDto reviewDto = reviewMapper.mapToDto(review);

        // 리뷰 이미지 리스트를 가져와서 DTO 리스트로 변환
        List<ReviewImage> reviewImageList = review.getReviewImages();
        List<ReviewImageDto> reviewImageDtoList = reviewImageService.convertToDtoList(reviewImageList);

        // User를 UserDto로 변환
        UserDto userDto = userMapper.mapToDto(review.getUser());

        // ReviewDto에 리뷰 이미지와 사용자 정보 설정
        reviewDto.setReviewImages(reviewImageDtoList);
        reviewDto.setUser(userDto);

        return reviewDto;
    }

    public Page<Review> searchAllPaginated(ReviewListSearchRequest reviewListSearchRequest, Pageable pageable) {
        return reviewRepository.findByCondition(reviewListSearchRequest, pageable);
    }
//    /**
//     * 문의 목록 검색
//     */
//    public ContactListResponse searchContactList(ContactListSearchRequest contactListSearchRequest, Pageable pageable) {
//        List<ContactDto> contactDtoList = new ArrayList<>();
//        Page<Contact> contactPage = contactRepository.findByCondition(contactListSearchRequest, pageable);
//
//        for (Contact contact : contactPage.getContent()) {
//            ContactDto contactDto = contactMapper.mapToDto(contact);
//            contactDto.setCountAnswer(contactRepository.countByRef(contact.getRef()) - 1);
//            contactDtoList.add(contactDto);
//        }
//
//        ContactListResponse contactListResponse = ContactListResponse.builder()
//                .contacts(contactDtoList)
//                .totalPages(contactPage.getTotalPages())
//                .build();
//
//        return contactListResponse;
//    }

    public void checkInput(ReviewEditRequest request) {
//        List<String> errorMessages = new ArrayList<>();

        // reviewId 체크
        if (request.getReviewId() == null || !isExistingById(request.getReviewId())) {
            throw new ValidationException("reviewIdInvalid", "수정 권한이 없습니다.");
//            errorMessages.add("Invalid reviewId");
        }

        // rating 체크
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ValidationException("ratingMissing", "평점을 선택해주세요.");
//            errorMessages.add("Rating should be between 1 and 5");
        }

        // title 체크
        if (request.getTitle() == null || StringUtils.isEmpty(request.getTitle().trim())) {
            throw new ValidationException("titleMissing", "제목을 입력해주세요.");
//            errorMessages.add("Title is missing");
        } else if (request.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new ValidationException("titleTooLong", "제목을 100자 이내로 입력해 주세요.");
//            errorMessages.add("Title should be within 100 characters");
        }

        // content 체크
        if (request.getContent() == null || StringUtils.isEmpty(request.getContent().trim())) {
            throw new ValidationException("contentMissing", "내용을 입력해주세요.");
//            errorMessages.add("Content is missing");
        } else if (request.getContent().length() > MAX_CONTENT_LENGTH) {
            throw new ValidationException("contentTooLong", "내용을 1000자 이내로 입력해 주세요.");
//            errorMessages.add("Content should be within 1000 characters");
        }

//        // 로그에 문제가 있으면 모든 로그 메시지 출력
//        if (!errorMessages.isEmpty()) {
//            for (String errorMessage : errorMessages) {
//                log.warn(errorMessage);
//            }
//            return false;
//        }
//        return true;
    }

    public void checkInput(ReviewWriteRequest request) {

        // orderId 체크
        if (request.getOrderId() == null || !orderService.isExistingById(request.getOrderId())) {
            throw new ValidationException("orderIdInvalid", "수정 권한이 없습니다.");
        }

        // rating 체크
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new ValidationException("ratingMissing", "평점을 선택해주세요.");
        }

        // title 체크
        if (request.getTitle() == null || StringUtils.isEmpty(request.getTitle().trim())) {
            throw new ValidationException("titleMissing", "제목을 입력해주세요.");
        } else if (request.getTitle().length() > MAX_TITLE_LENGTH) {
            throw new ValidationException("titleTooLong", "제목을 100자 이내로 입력해 주세요.");
        }

        // content 체크
        if (request.getContent() == null || StringUtils.isEmpty(request.getContent().trim())) {
            throw new ValidationException("contentMissing", "내용을 입력해주세요.");
        } else if (request.getContent().length() > MAX_CONTENT_LENGTH) {
            throw new ValidationException("contentTooLong", "내용을 1000자 이내로 입력해 주세요.");
        }
    }

    @Transactional
    public Review insertReview(Long userId, ReviewWriteRequest request) {

        User user = userService.retrieveById(userId);
        Order order = orderService.retrieveById(request.getOrderId());

        Review review = Review.builder()
                .user(user)
                .order(order)
                .rating(request.getRating())
                .title(request.getTitle())
                .content(request.getContent())
                .activateFlag(true)
                .build();

        reviewRepository.save(review);

        return review;
    }

    @Transactional
    public Review updateReview(ReviewEditRequest request) {

        // 1. 데이터베이스에서 기존 Review 객체를 가져옵니다.
        Review review = retrieveById(request.getReviewId());

        // 2. 가져온 Review 객체의 속성을 갱신합니다.
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());

        // 3. 갱신된 Review 객체를 데이터베이스에 다시 저장합니다.
        reviewRepository.save(review);

        return review;
    }

    /**
     * 주어진 리뷰 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param review    업데이트할 리뷰 정보 엔티티
     * @param reviewDto 업데이트에 사용될 DTO
     */
    @Transactional
    public void updateReviewByAdmin(Review review, ReviewDto reviewDto) {

        // 변경 사항을 감지하여 엔티티를 저장
        if (updateReviewDetails(review, reviewDto)) {
            reviewRepository.save(review);
        }
    }

    /**
     * 주어진 리뷰 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param reviewDtoList 업데이트할 리뷰 정보의 DTO 목록
     */
    @Transactional
    public void updateReviewsByAdmin(List<ReviewDto> reviewDtoList) {

        for (ReviewDto reviewDto : reviewDtoList) {
            // ID를 이용해 리뷰 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Review review = reviewRepository.findById(reviewDto.getId())
                    .orElseThrow(() -> new NotFoundException("reviewNotFound", "공지 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateReviewDetails(review, reviewDto)) {
                reviewRepository.save(review);
            }
        }
    }

    /**
     * 개별 리뷰 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param review    업데이트할 리뷰 정보 엔티티
     * @param reviewDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateReviewDetails(Review review, ReviewDto reviewDto) {
        boolean isModified = false;

        // activateFlag 상태 변경이 있을 경우 업데이트
        if (reviewDto.isActivateFlag() != review.isActivateFlag()) {
            review.setActivateFlag(reviewDto.isActivateFlag());
            isModified = true; // 상태가 변경되었다면 수정됨으로 표시
        }

        // 변경된 사항이 있으면 true, 아니면 false를 반환
        return isModified;
    }
}