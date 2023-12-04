package com.bridgeshop.module.product.service;

import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.module.category.repository.CategoryRepository;
import com.bridgeshop.module.product.dto.ProductImageDto;
import com.bridgeshop.module.product.dto.ProductUpsertRequest;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductImage;
import com.bridgeshop.module.product.mapper.ProductImageMapper;
import com.bridgeshop.module.product.repository.ProductImageRepository;
import com.bridgeshop.common.service.FileUploadService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    private final FileUploadService fileUploadService;

    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;

    private final ProductImageMapper productImageMapper;

    @Value("${upload.directory.product}")
    private String uploadDir;

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

            // 원본 파일 이름에서 파일 확장자 추출
            String extension = extractFileExtension(file.getOriginalFilename());
            // 저장할 파일 이름 생성
            String fileName = createFileName(productCategoryName, userId, extension);

            // ProductImage 설정
            ProductImage productImage = new ProductImage();

            productImage.setProduct(product);
            productImage.setFilePath("/img/upload/products/");
            productImage.setFileName(fileName);
            productImage.setDisplayOrder(i + 1);

            productImageList.add(productImage);

            // 입력 데이터 File 서버에 업로드
            fileUploadService.uploadFile(file, fileName, "products");
        }
        productImageRepository.saveAll(productImageList);

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

                // 원본 파일 이름에서 파일 확장자 추출
                String extension = extractFileExtension(file.getOriginalFilename());
                // 저장할 파일 이름 생성
                String fileName = createFileName(productCategoryName, userId, extension);

                ProductImage productImage = new ProductImage();

                productImage.setProduct(product);
                productImage.setFilePath("/img/upload/products/");
                productImage.setFileName(fileName);
                productImage.setDisplayOrder(i + 1);

                productImageList.add(productImage);

                // 변경 후 이미지 업로드 (파일 시스템)
                fileUploadService.uploadFile(file, fileName, "products"); // 입력 데이터 File 서버에 업로드
            }

            // 변경 전 이미지 삭제 (파일 시스템)
            for (ProductImage productImage : product.getProductImages()) {
                fileUploadService.deleteFile(uploadDir + productImage.getFileName());
            }

            // 변경 전 이미지 삭제 및 변경 후 이미지 삽입 (파일 DB)
            productImageRepository.deleteAllByProduct_Id(product.getId());
            productImageRepository.saveAll(productImageList);
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
    private String createFileName(String productCategoryName, Long userId, String extension) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 년월일시분초 밀리초 포맷
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
        String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환

        return "product_" + productCategoryName + "_" + userId + "_" + formattedDate + extension;
    }
}