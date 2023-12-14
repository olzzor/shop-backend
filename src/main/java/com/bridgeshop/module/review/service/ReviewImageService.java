package com.bridgeshop.module.review.service;

import com.bridgeshop.common.service.FileUploadService;
import com.bridgeshop.module.review.entity.Review;
import com.bridgeshop.module.review.entity.ReviewImage;
import com.bridgeshop.module.review.dto.ReviewImageDto;
import com.bridgeshop.module.review.mapper.ReviewImageMapper;
import com.bridgeshop.module.review.repository.ReviewImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewImageService {

    private final FileUploadService fileUploadService;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewImageMapper reviewImageMapper;

    public List<ReviewImage> retrieveAllByReviewId(Long id) {
        return reviewImageRepository.findAllByReview_Id(id);
    }

    /**
     * 리뷰 이미지 취득
     */
    public ReviewImageDto convertToDto(ReviewImage reviewImage) {
        return reviewImageMapper.mapToDto(reviewImage);
    }

    /**
     * 리뷰 이미지 리스트 취득
     */
    public List<ReviewImageDto> convertToDtoList(List<ReviewImage> reviewImageList) {
        return reviewImageMapper.mapToDtoList(reviewImageList);
    }

    @Transactional
    public void saveReviewImages(Long userId, Review review, List<MultipartFile> files) {

        List<ReviewImage> reviewImageList = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);

            // 원본 파일 이름에서 파일 확장자 추출
            String extension = extractFileExtension(file.getOriginalFilename());
            // 저장할 파일 이름 생성
            String fileName = createFileName(userId, extension);

            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .filePath("/img/upload/reviews/")
                    .fileName(fileName)
                    .displayOrder(i + 1)
                    .build();

            reviewImageList.add(reviewImage);

            fileUploadService.uploadFile(file, fileName, "reviews");
        }

        reviewImageRepository.saveAll(reviewImageList);
    }

    @Transactional
    public void addReviewImages(Long userId,
                                Review review,
                                List<MultipartFile> files,
                                List<ReviewImageDto> existingImages) {

        // 3. 추가되어야 할 새로운 이미지를 찾습니다.
        List<ReviewImageDto> imagesToAdd = existingImages.stream()
                .filter(ei -> ei.getId() == null)
                .collect(Collectors.toList());


        // 4. 새로운 이미지를 추가합니다.
        List<ReviewImage> reviewImageList = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);

            // 원본 파일 이름에서 파일 확장자 추출
            String extension = extractFileExtension(file.getOriginalFilename());
            // 저장할 파일 이름 생성
            String fileName = createFileName(userId, extension);

            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .filePath("/img/upload/reviews/")
                    .fileName(fileName)
                    .displayOrder(imagesToAdd.get(i).getDisplayOrder())
                    .build();

            reviewImageList.add(reviewImage);

            fileUploadService.uploadFile(file, fileName, "reviews");
        }

        reviewImageRepository.saveAll(reviewImageList);
    }

    @Transactional
    public void updateReviewImages(List<ReviewImage> currentImages,
                                   List<ReviewImageDto> existingImages) {

        // 5. displayOrder 만 갱신해야 하는 이미지를 찾습니다.
        List<ReviewImageDto> imagesToUpdate = existingImages.stream()
                .filter(ei -> ei.getId() != null)
                .collect(Collectors.toList());

        // 6. displayOrder 를 갱신합니다.
        for (ReviewImageDto imageToUpdate : imagesToUpdate) {
            ReviewImage existingImage = currentImages.stream()
                    .filter(ci -> ci.getId().equals(imageToUpdate.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingImage != null) {
                existingImage.setDisplayOrder(imageToUpdate.getDisplayOrder());
                reviewImageRepository.save(existingImage);
            }
        }
    }

    @Transactional
    public void deleteReviewImages(List<ReviewImage> currentImages,
                                       List<ReviewImageDto> existingImages) {

        // 1. 기존 이미지 중에 삭제되어야 할 이미지를 찾습니다.
        List<ReviewImage> imagesToDelete = currentImages.stream()
                .filter(ci -> existingImages.stream().noneMatch(ei -> ei.getId() != null && ei.getId().equals(ci.getId())))
                .collect(Collectors.toList());

        // 2. 이미지를 삭제합니다.
        for (ReviewImage imageToDelete : imagesToDelete) {
            reviewImageRepository.delete(imageToDelete);
            fileUploadService.deleteFile(imageToDelete.getFileName());
        }
    }

    /**
     * 파일 확장자 추출 메소드
     */
    private String extractFileExtension(String originalFileName) {
        int lastDot = originalFileName.lastIndexOf('.');

        return (lastDot > 0) ? originalFileName.substring(lastDot) : "";
    }

    /**
     * 파일 이름 생성 메소드
     */
    private String createFileName(Long userId, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 년월일시분초 밀리초 포맷
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
        String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

        return "review_" + userId + "_" + formattedDate + extension;
    }
}