package com.bridgeshop.module.review.service;

import com.bridgeshop.common.service.S3UploadService;
import com.bridgeshop.module.review.dto.ReviewImageDto;
import com.bridgeshop.module.review.entity.Review;
import com.bridgeshop.module.review.entity.ReviewImage;
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

    private final S3UploadService s3UploadService;
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

            String extension = extractFileExtension(file.getOriginalFilename()); // 원본 파일 이름에서 파일 확장자 추출
            String fileName = createFileName(userId, extension); // 저장할 파일 이름 생성
            String fileKey = "reviews/" + fileName; // S3에 저장될 파일의 키를 'reviews/' 디렉토리와 생성된 파일 이름으로 결합
            String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환

            // ReviewImage 객체 생성 및 리스트에 추가
            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .fileUrl(s3Url)
                    .fileKey(fileKey)
                    .displayOrder(i + 1)
                    .build();

            reviewImageList.add(reviewImage);
        }

        reviewImageRepository.saveAll(reviewImageList); // 생성된 ReviewImage 리스트를 DB에 저장
    }

    @Transactional
    public void addReviewImages(Long userId, Review review,
                                List<MultipartFile> files, List<ReviewImageDto> existingImages) {

        // 추가되어야 할 새로운 이미지를 필터
        List<ReviewImageDto> imagesToAdd = existingImages.stream()
                .filter(ei -> ei.getId() == null)
                .collect(Collectors.toList());

        // 이미지 추가
        List<ReviewImage> reviewImageList = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);

            String extension = extractFileExtension(file.getOriginalFilename()); // 원본 파일 이름에서 파일 확장자 추출
            String fileName = createFileName(userId, extension); // 저장할 파일 이름 생성
            String fileKey = "reviews/" + fileName; // S3에 저장될 파일의 키를 'reviews/' 디렉토리와 생성된 파일 이름으로 결합
            String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환

            ReviewImage reviewImage = ReviewImage.builder()
                    .review(review)
                    .fileUrl(s3Url)
                    .fileKey(fileKey)
                    .displayOrder(imagesToAdd.get(i).getDisplayOrder())
                    .build();

            reviewImageList.add(reviewImage);
        }

        reviewImageRepository.saveAll(reviewImageList); // 생성된 ReviewImage 리스트를 DB에 저장
    }

    @Transactional
    public void updateReviewImages(List<ReviewImage> currentImages, List<ReviewImageDto> existingImages) {

        // displayOrder 를 갱신할 이미지를 필터
        List<ReviewImageDto> reviewImageDtoListToUpdate = existingImages.stream()
                .filter(ei -> ei.getId() != null)
                .collect(Collectors.toList());

        // displayOrder 를 갱신
        for (ReviewImageDto reviewImageDto : reviewImageDtoListToUpdate) {
            ReviewImage existingImage = currentImages.stream()
                    .filter(ci -> ci.getId().equals(reviewImageDto.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingImage != null) {
                existingImage.setDisplayOrder(reviewImageDto.getDisplayOrder());
                reviewImageRepository.save(existingImage);
            }
        }
    }

    @Transactional
    public void deleteReviewImages(List<ReviewImage> currentImages, List<ReviewImageDto> existingImages) {

        // 삭제되어야 할 이미지를 필터
        List<ReviewImage> reviewImageListToDelete = currentImages.stream()
                .filter(ci -> existingImages.stream().noneMatch(ei -> ei.getId() != null && ei.getId().equals(ci.getId())))
                .collect(Collectors.toList());

        // 이미지 삭제
        for (ReviewImage reviewImage : reviewImageListToDelete) {
            reviewImageRepository.delete(reviewImage);
            s3UploadService.deleteImage(reviewImage.getFileKey());
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
     * 사용자 ID, 현재 날짜와 시간을 조합하여 파일 이름 생성
     */
    private String createFileName(Long userId, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 날짜 포맷 설정
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
        String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

        // 사용자 ID, 현재 날짜와 시간, 파일 확장자를 결합하여 파일 이름 생성
        return userId + "_" + formattedDate + extension;
    }
}