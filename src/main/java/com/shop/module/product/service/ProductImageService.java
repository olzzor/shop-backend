package com.shop.module.product.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.service.S3UploadService;
import com.shop.module.category.repository.CategoryRepository;
import com.shop.module.product.dto.ProductImageDto;
import com.shop.module.product.dto.ProductUpsertRequest;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductImage;
import com.shop.module.product.mapper.ProductImageMapper;
import com.shop.module.product.repository.ProductImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final S3UploadService s3UploadService;

    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    public List<ProductImage> retrieveAllByProductId(Long productId) {
        return productImageRepository.findAllByProduct_Id(productId);
    }

    /**
     * 상품 파일 DTO 변환
     */
    public ProductImageDto convertToDto(ProductImage productImage) {
        return productImageMapper.mapToDto(productImage);
    }

    /**
     * 상품 파일 리스트 DTO 변환
     */
    public List<ProductImageDto> convertToDtoList(List<ProductImage> productImageList) {
        return productImageMapper.mapToDtoList(productImageList);
    }

    /**
     * 상품 파일 리스트 취득 (대표 이미지만)
     */
    public List<ProductImageDto> getProductImageDtoListForDisplay(List<ProductImage> productImageList) {

        return productImageList.stream()
                .filter(productImage -> productImage.getDisplayOrder() == 1)
                .map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public List<ProductImage> saveProductImages(Long userId, Product product, List<MultipartFile> files) {

        List<ProductImage> productImageList = new ArrayList<>();
        String productCategoryName = product.getCategory().getName();

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            String extension = extractFileExtension(file.getOriginalFilename()); // 원본 파일 이름에서 파일 확장자 추출
            String fileName = createFileName(productCategoryName, userId, extension); // 상품 카테고리, 등록 관리자 ID, 현재 시간을 이용하여 고유한 파일 이름 생성
            String fileKey = "products/" + fileName; // S3에 저장될 파일의 키를 'products/' 디렉토리와 생성된 파일 이름으로 결합
            String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환

            // ProductImage 객체 생성 및 리스트에 추가
            ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .fileUrl(s3Url)
                    .fileKey(fileKey)
                    .displayOrder(i + 1)
                    .build();

            productImageList.add(productImage);
        }
        productImageRepository.saveAll(productImageList); // 생성된 ProductImage 리스트를 DB에 저장

        return productImageList;
    }

    @Transactional
    public void updateProductImages(Long userId, Product product, ProductUpsertRequest productUpdReq, List<MultipartFile> files) throws IOException {

        // 업로드 된 이미지가 있는 경우
        if (files != null && !files.isEmpty()) {

            // 입력값으로 기존 정보를 업데이트
            List<ProductImage> productImageList = new ArrayList<>();

            for (int i = 0; i < files.size(); i++) {

                String productCategoryName = categoryRepository.findNameByCode(productUpdReq.getCategoryCode())
                        .orElseThrow(() -> new NotFoundException("categoryNotFound", "카테고리 정보를 찾을 수 없습니다."));

                MultipartFile file = files.get(i);

                String extension = extractFileExtension(file.getOriginalFilename()); // 원본 파일 이름에서 파일 확장자 추출
                String fileName = createFileName(productCategoryName, userId, extension); // 저장할 파일 이름 생성
                String fileKey = "products/" + fileName; // S3에 저장될 파일의 키를 'products/' 디렉토리와 생성된 파일 이름으로 결합
                String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환

                ProductImage productImage = ProductImage.builder()
                        .product(product)
                        .fileUrl(s3Url)
                        .fileKey(fileKey)
                        .displayOrder(i + 1)
                        .build();

                productImageList.add(productImage);
            }

            for (ProductImage productImage : product.getProductImages()) {
                s3UploadService.deleteImage(productImage.getFileKey()); // S3에 업로드 된 변경 전 이미지 파일 삭제
            }

            productImageRepository.deleteAllByProduct_Id(product.getId()); // 변경 전 ProductImage 리스트를 DB에서 삭제
            productImageRepository.saveAll(productImageList); // 변경 후 ProductImage 리스트를 DB에 저장
        }
    }

    @Transactional
    public void addProductImages(Long userId, Product product,
                                List<MultipartFile> files, List<ProductImageDto> existingImages) {

        // 추가되어야 할 새로운 이미지를 필터
        List<ProductImageDto> imagesToAdd = existingImages.stream()
                .filter(ei -> ei.getId() == null)
                .collect(Collectors.toList());

        // 이미지 추가
        List<ProductImage> productImageList = new ArrayList<>();

        for (int i = 0; i < files.size(); i++) {

            MultipartFile file = files.get(i);

            String extension = extractFileExtension(file.getOriginalFilename()); // 원본 파일 이름에서 파일 확장자 추출
            String fileName = createFileName(product.getCategory().getName(), userId, extension); // 저장할 파일 이름 생성
            String fileKey = "products/" + fileName; // S3에 저장될 파일의 키를 'products/' 디렉토리와 생성된 파일 이름으로 결합
            String s3Url = s3UploadService.saveFile(file, fileKey); // S3에 파일 업로드 및 URL 반환

            ProductImage productImage = ProductImage.builder()
                    .product(product)
                    .fileUrl(s3Url)
                    .fileKey(fileKey)
                    .displayOrder(imagesToAdd.get(i).getDisplayOrder())
                    .build();

            productImageList.add(productImage);
        }

        productImageRepository.saveAll(productImageList); // 생성된 ProductImage 리스트를 DB에 저장
    }

    @Transactional
    public void updateProductImages(List<ProductImage> currentImages, List<ProductImageDto> existingImages) {

        // displayOrder 를 갱신할 이미지를 필터
        List<ProductImageDto> productImageDtoListToUpdate = existingImages.stream()
                .filter(ei -> ei.getId() != null)
                .collect(Collectors.toList());

        // displayOrder 를 갱신
        for (ProductImageDto productImageDto : productImageDtoListToUpdate) {
            ProductImage existingImage = currentImages.stream()
                    .filter(ci -> ci.getId().equals(productImageDto.getId()))
                    .findFirst()
                    .orElse(null);

            if (existingImage != null) {
                existingImage.setDisplayOrder(productImageDto.getDisplayOrder());
                productImageRepository.save(existingImage);
            }
        }
    }

    @Transactional
    public void deleteProductImages(List<ProductImage> currentImages, List<ProductImageDto> existingImages) {

        // 삭제되어야 할 이미지를 필터
        List<ProductImage> productImageListToDelete = currentImages.stream()
                .filter(ci -> existingImages.stream().noneMatch(ei -> ei.getId() != null && ei.getId().equals(ci.getId())))
                .collect(Collectors.toList());

        // 이미지 삭제
        for (ProductImage productImage : productImageListToDelete) {
            productImageRepository.delete(productImage);
            s3UploadService.deleteImage(productImage.getFileKey());
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
     * 상품 카테고리 이름, 사용자 ID, 현재 날짜와 시간을 조합하여 파일 이름 생성
     */
    private String createFileName(String productCategoryName, Long userId, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 날짜 포맷 설정
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
        String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

        // 카테고리 이름, 사용자 ID, 현재 날짜와 시간, 파일 확장자를 결합하여 파일 이름 생성
        return productCategoryName + "_" + userId + "_" + formattedDate + extension;
    }
}