package com.shop.module.product.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.dto.ProductSizeUpsertRequest;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.product.mapper.ProductSizeMapper;
import com.shop.module.product.repository.ProductSizeRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductSizeService {

    private final ProductSizeRepository productSizeRepository;
    private final ProductSizeMapper productSizeMapper;

    public ProductSize retrieveById(Long id) {
        return productSizeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("productSizeNotFound", "상품 사이즈 정보를 찾을 수 없습니다."));
    }

    public List<ProductSize> retrieveAllByProductId(Long productId) {
        return productSizeRepository.findAllByProduct_Id(productId);
    }

    /**
     * 상품 사이즈 DTO 변환
     */
    public ProductSizeDto convertToDto(ProductSize productSize) {
        return productSizeMapper.mapToDto(productSize);
    }

    /**
     * 상품 사이즈 리스트 DTO 변환
     */
    public List<ProductSizeDto> convertToDtoList(List<ProductSize> productSizeList) {
        return productSizeMapper.mapToDtoList(productSizeList);
    }

    public void checkInputs(List<ProductSizeUpsertRequest> productSizeUpsReqList) {
        if (productSizeUpsReqList.isEmpty()) {
            throw new ValidationException("sizeListEmpty", "하나 이상의 사이즈 정보가 필요합니다.");
        }

        productSizeUpsReqList.forEach(this::checkInput);
    }

    public void checkInput(ProductSizeUpsertRequest productSizeUpsReq) {

        if (StringUtils.isBlank(productSizeUpsReq.getSize())) {
            throw new ValidationException("sizeMissing", "사이즈를 입력해주세요.");
        }

        if (productSizeUpsReq.getQuantity() < 0) {
            throw new ValidationException("quantityInvalid", "수량은 0 이상이어야 합니다.");
        }
    }

    @Transactional
    public List<ProductSize> insertProductSizes(Product product, List<ProductSizeUpsertRequest> productSizeUpsReqList) {

        List<ProductSize> productSizeList = new ArrayList<>();

        for (ProductSizeUpsertRequest productSizeUpsReq : productSizeUpsReqList) {

            ProductSize productSize = ProductSize.builder()
                    .product(product) // 연관된 Product의 ID 설정
                    .size(productSizeUpsReq.getSize()) // 사이즈 설정
                    .quantity(productSizeUpsReq.getQuantity()) // 수량 설정
                    .build();

            // 저장된 ProductSize 엔터티를 DB에 저장하고 반환된 결과를 리스트에 추가
            productSizeList.add(productSizeRepository.save(productSize));
        }

        return productSizeList;
    }


    /**
     * 주어진 상품 사이즈 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param updateProductSizeList 업데이트할 공지 정보의 DTO 목록
     */
    @Transactional
    public void updateProductSizes(List<ProductSizeUpsertRequest> updateProductSizeList) {

        for (ProductSizeUpsertRequest updateProductSize : updateProductSizeList) {
            // ID를 이용해 상품 사이즈 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            ProductSize productSize = productSizeRepository.findById(updateProductSize.getId())
                    .orElseThrow(() -> new NotFoundException("productSizeNotFound", "상품 사이즈 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 갱신
            if (updateProductSizeDetails(productSize, updateProductSize)) {
                productSizeRepository.save(productSize);
            }
        }
    }

    /**
     * 개별 상품 사이즈 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param productSize      업데이트할 상품 사이즈 정보 엔티티
     * @param productSizeAfter 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    public boolean updateProductSizeDetails(ProductSize productSize, ProductSizeUpsertRequest productSizeAfter) {
        boolean isModified = false;

        // 상품 사이즈 재고 변경이 있을 경우 업데이트
        if (productSizeAfter.getQuantity() != productSizeAfter.getAdjustmentQuantity()) {
            int newQuantity = productSize.getQuantity() + productSizeAfter.getAdjustmentQuantity() - productSizeAfter.getQuantity();
            productSize.setQuantity(newQuantity < 0 ? 0 : newQuantity);
            isModified = true;
        }

        // 변경된 사항이 있으면 true, 아니면 false를 반환
        return isModified;
    }

    @Transactional
    public void decreaseProductSizeQuantity(ProductSize productSize, int quantity) {
        productSize.setQuantity(productSize.getQuantity() - quantity);
        productSizeRepository.save(productSize);
    }
}