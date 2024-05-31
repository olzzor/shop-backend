package com.shop.module.product.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.module.product.dto.ProductDetailDto;
import com.shop.module.product.dto.ProductDetailUpsertRequest;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductDetail;
import com.shop.module.product.mapper.ProductDetailMapper;
import com.shop.module.product.repository.ProductDetailRepository;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDetailService {

    private final ProductDetailRepository productDetailRepository;
    private final ProductDetailMapper productDetailMapper;

    private static final int MAX_DESCRIPTION_LENGTH = 2000;
    private static final int MAX_CONTENT_LENGTH = 10000;

    public ProductDetail retrieveById(Long id) {
        return productDetailRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("productDetailNotFound", "상품 상세 정보를 찾을 수 없습니다."));
    }

    public List<ProductDetail> retrieveAllByProductId(Long productId) {
        return productDetailRepository.findAllByProduct_Id(productId);
    }

    /**
     * 상품 사이즈 DTO 변환
     */
    public ProductDetailDto convertToDto(ProductDetail productDetail) {
        return productDetailMapper.mapToDto(productDetail);
    }

    public void checkInput(ProductDetailUpsertRequest productDetailUpsReq) {
        validateDescription(productDetailUpsReq.getDescription());  // description 체크
    }

    @Transactional
    public ProductDetail insertProductDetail(Product product, ProductDetailUpsertRequest productDetailInsReq) {

        ProductDetail productDetail = ProductDetail.builder()
                .product(product) // 연관된 Product의 ID 설정
                .description(productDetailInsReq.getDescription()) // 제품 설명 설정
                .content(productDetailInsReq.getContent()) // 사이즈 가이드 설정
                .build();

        return productDetailRepository.save(productDetail);
    }

    /**
     * 주어진 상품 상세 내용에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param productDetail    업데이트할 공지 정보 엔티티
     * @param productDetailDto 업데이트에 사용될 DTO
     */
    @Transactional
    public void updateProductDetailContent(ProductDetail productDetail, ProductDetailDto productDetailDto) {
        boolean isModified = false;

        if (!Objects.equals(productDetail.getContent(), productDetailDto.getContent().trim())) {
            validateContent(productDetailDto.getContent());
            productDetail.setContent(productDetailDto.getContent());
            isModified = true;
        }

        // 변경 사항을 감지하여 엔티티를 저장
        if (isModified) {
            productDetailRepository.save(productDetail);
        }
    }

    /**
     * 주어진 상품 상세 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param productDetailUpdReq 업데이트할 상품 상세 정보
     */
    @Transactional
    public void updateProductDetail(ProductDetailUpsertRequest productDetailUpdReq) {

        // ID를 이용해 상품 사이즈 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
        ProductDetail productDetail = productDetailRepository.findById(productDetailUpdReq.getId())
                .orElseThrow(() -> new NotFoundException("productDetailNotFound", "상품 상세 정보를 찾을 수 없습니다."));

        // 변경 사항을 감지하여 엔티티를 갱신
        if (updateProductDetailDetails(productDetail, productDetailUpdReq)) {
            productDetailRepository.save(productDetail);
        }
    }

    /**
     * 개별 상품 상세 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param productDetail       업데이트할 상품 상세 정보 엔티티
     * @param productDetailUpdReq 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    public boolean updateProductDetailDetails(ProductDetail productDetail, ProductDetailUpsertRequest productDetailUpdReq) {
        boolean isModified = false;

        // 제품 설명, 사이즈 가이드의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(productDetail.getDescription(), productDetailUpdReq.getDescription(), productDetail::setDescription);
        isModified |= updateIfDifferent(productDetail.getContent(), productDetailUpdReq.getContent(), productDetail::setContent);

        // 변경된 사항이 있으면 true, 아니면 false를 반환
        return isModified;
    }

    /**
     * 현재 값과 새로운 값을 비교하여 다를 경우, 제공된 setter 함수를 사용해 값을 업데이트
     *
     * @param currentValue 현재 객체의 필드 값
     * @param newValue     업데이트 할 새로운 값
     * @param setter       현재 값을 업데이트할 setter 메소드 참조
     * @return 값이 변경되었으면 true를, 그렇지 않으면 false를 반환
     */
    private boolean updateIfDifferent(String currentValue, String newValue, Consumer<String> setter) {
        if (StringUtils.isNotBlank(newValue) && !newValue.trim().equals(currentValue)) {
            // 새로운 값이 다르다면 setter 메소드를 사용하여 현재 객체의 값을 업데이트
            setter.accept(newValue.trim());
            return true; // 변경이 있었으므로 true를 반환
        }
        return false; // 값이 변경되지 않았으므로 false를 반환
    }

    /**
     * description 필드 검증
     */
    private void validateDescription(String description) {
        if (StringUtils.isBlank(description)) {
            throw new ValidationException("descriptionMissing", "제품 설명을 입력해주세요.");
        } else if (description.trim().length() > MAX_DESCRIPTION_LENGTH) {
            throw new ValidationException("descriptionTooLong", "제품 설명은 " + String.format("%,d", MAX_DESCRIPTION_LENGTH) + "자 이하로 입력해주세요.");
        }
    }

    /**
     * content 필드 검증
     */
    private void validateContent(String content) {
        if (StringUtils.isBlank(content)) {
            throw new ValidationException("contentMissing", "내용을 입력해주세요.");
        } else if (content.trim().length() > MAX_CONTENT_LENGTH) {
            throw new ValidationException("contentTooLong", "내용은 " + String.format("%,d", MAX_CONTENT_LENGTH) + "자 이하로 입력해주세요.");
        }
    }
}