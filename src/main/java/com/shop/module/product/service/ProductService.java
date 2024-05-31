package com.shop.module.product.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.module.category.dto.CategoryDto;
import com.shop.module.category.entity.Category;
import com.shop.module.category.mapper.CategoryMapper;
import com.shop.module.category.repository.CategoryRepository;
import com.shop.module.notice.dto.NoticeDto;
import com.shop.module.notice.entity.Notice;
import com.shop.module.product.dto.*;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductStatus;
import com.shop.module.product.mapper.ProductDetailMapper;
import com.shop.module.product.mapper.ProductImageMapper;
import com.shop.module.product.mapper.ProductMapper;
import com.shop.module.product.mapper.ProductSizeMapper;
import com.shop.module.product.repository.ProductRepository;
import com.shop.module.recommendedproduct.repository.RecommendedProductRepository;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final RecommendedProductRepository recommendedProductRepository;

    private final ProductMapper productMapper;
    private final ProductSizeMapper productSizeMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductDetailMapper productDetailMapper;
    private final CategoryMapper categoryMapper;

    private static final int MAX_NAME_LENGTH = 100;

    public boolean existProduct(Long id) {
        return productRepository.existsById(id);
    }

    public Product retrieveById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("productNotFound", "상품 정보를 찾을 수 없습니다."));
    }

    public List<Product> retrieveAllByIds(List<Long> ids) {
        return productRepository.findAllByIdIn(ids);
    }

    /**
     * 상품 DTO 변환
     */
    public ProductDto convertToDto(Product product) {
        return productMapper.mapToDto(product);
    }

    /**
     * 상품 리스트 DTO 변환
     */
    public List<ProductDto> convertToDtoList(List<Product> productList) {
        return productMapper.mapToDtoList(productList);
    }

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAllByIsDisplayAndProductImages_DisplayOrder(true, 1, pageable);
    }

    public Page<Product> searchProducts(String query, Pageable pageable) {
        return productRepository.searchByKeywords(query, pageable);
    }

    public Page<Product> getProductsByMainCategory(String categoryCode, Pageable pageable) {
        return productRepository.findAllByIsDisplayAndCategory_CodeRefAndProductImages_DisplayOrder(true, categoryCode, 1, pageable);
    }

    public Page<Product> getProductsBySubCategory(String categoryCode, Pageable pageable) {
        return productRepository.findAllByIsDisplayAndCategory_CodeAndProductImages_DisplayOrder(true, categoryCode, 1, pageable);
    }

    public Page<Product> retrieveAllPaginated(Pageable pageable) {
        return productRepository.findAllByProductImages_DisplayOrder(1, pageable);
    }

    // 해당 상품의 이미지 파일 리스트 취득 (대표 이미지만 취득) 및 설정
    public ProductDto getDtoWithMainImage(Product product) {
        ProductDto productDto = productMapper.mapToDto(product);

        // 해당 상품의 이미지 파일 중 displayOrder가 1인 파일만 필터링하여 DTO 변환
        List<ProductImageDto> productImageDtoList = product.getProductImages().stream()
                .filter(productImage -> productImage.getDisplayOrder() == 1) // displayOrder가 1인 ProductImage만 필터링
                .map(productImageMapper::mapToDto) // 필터링된 ProductImage을 ProductImageDto로 매핑
                .collect(Collectors.toList()); // 결과를 리스트로 수집

        // 변환된 DTO 리스트를 productDto에 설정
        productDto.setProductImages(productImageDtoList);

        return productDto;
    }

    public List<ProductDto> getDtoListWithMainImage(List<Product> productList) {
        return productList.stream().map(this::getDtoWithMainImage).collect(Collectors.toList());
    }

    public ProductDto getDtoWithCategoryAndSizeAndMainImage(Product product) {
        ProductDto productDto = productMapper.mapToDto(product);

        // 해당 상품의 카테고리 정보 취득 및 설정
        CategoryDto categoryDto = categoryMapper.mapToDto(product.getCategory());
        productDto.setCategory(categoryDto);

        // 해당 상품의 사이즈 리스트 취득 및 설정
        List<ProductSizeDto> productSizeDtoList = productSizeMapper.mapToDtoList(product.getProductSizes());
        productDto.setProductSizes(productSizeDtoList);

        // 해당 상품의 이미지 파일 중 displayOrder가 1인 파일만 필터링하여 DTO 변환
        List<ProductImageDto> productImageDtoList = product.getProductImages().stream()
                .filter(productImage -> productImage.getDisplayOrder() == 1) // displayOrder가 1인 ProductImage만 필터링
                .map(productImageMapper::mapToDto) // 필터링된 ProductImage을 ProductImageDto로 매핑
                .collect(Collectors.toList()); // 결과를 리스트로 수집

        // 변환된 DTO 리스트를 productDto에 설정
        productDto.setProductImages(productImageDtoList);

        return productDto;
    }

    public List<ProductDto> getDtoListWithCategoryAndSizeMainImage(List<Product> productList) {
        return productList.stream().map(this::getDtoWithCategoryAndSizeAndMainImage).collect(Collectors.toList());
    }

    public ProductDto getDtoWithCategoryAndSizeAndImageAndDetail(Product product) {
        ProductDto productDto = productMapper.mapToDto(product);

        // 해당 상품의 카테고리 정보 취득 및 설정
        CategoryDto categoryDto = categoryMapper.mapToDto(product.getCategory());
        productDto.setCategory(categoryDto);

        // 해당 상품의 사이즈 리스트 취득 및 설정
        List<ProductSizeDto> productSizeDtoList = productSizeMapper.mapToDtoList(product.getProductSizes());
        productDto.setProductSizes(productSizeDtoList);

        // 해당 상품의 이미지 파일 리스트 취득 및 설정
        List<ProductImageDto> productImageDtoList = productImageMapper.mapToDtoList(product.getProductImages());
        productDto.setProductImages(productImageDtoList);

        // 해당 상품의 상세 설명 취득 및 설정
        ProductDetailDto productDetailDto = productDetailMapper.mapToDto(product.getProductDetail());
        productDto.setDetail(productDetailDto);

        return productDto;
    }

    public Page<Product> searchProductList(ProductListSearchRequest productListSearchRequest, Pageable pageable) {
        return productRepository.findByCondition(productListSearchRequest, pageable);
    }

    public Page<Product> searchProductListExcludingRecommended(ProductListSearchRequest productListSearchRequest, Pageable pageable) {
        List<Long> recommendedProductIds = recommendedProductRepository.findAllProductIds();

        return productRepository.findByConditionExcludingRecommended(productListSearchRequest, recommendedProductIds, pageable);
    }

    public Page<Product> searchAllProductListExcludingRecommended(Pageable pageable) {
        List<Long> recommendedProductIds = recommendedProductRepository.findAllProductIds();

        return productRepository.findAllExcludingRecommended(recommendedProductIds, pageable);
    }

    public void checkInput(ProductUpsertRequest productUpsReq) {
        validateCategoryCode(productUpsReq.getCategoryCode());  // categoryCode 체크
        validateName(productUpsReq.getName());                  // name 체크
        validatePrice(productUpsReq.getPrice());                // price 체크
        validateDiscountPer(productUpsReq.getDiscountPer());    // discountPer 체크
    }

    @Transactional
    public Product insertProduct(Long userId, ProductUpsertRequest productUpsReq) {

        try {
            Category category = categoryRepository.findByCode(productUpsReq.getCategoryCode())
                    .orElseThrow(() -> new NotFoundException("categoryNotFound", "카테고리 정보를 찾을 수 없습니다."));

            // 상품 코드 작성
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS"); // 년월일시분초 포맷
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Seoul")); // 시간대 설정
            String formattedDate = sdf.format(new Date()); // 현재 날짜와 시간을 위의 포맷으로 변환
            String productCode = category.getCode() + "_" + userId + "_" + formattedDate;

            Product product = Product.builder()
                    .code(productCode)
                    .name(productUpsReq.getName())
                    .category(category)
                    .price(productUpsReq.getPrice())
                    .discountPer(productUpsReq.getDiscountPer())
                    .isDisplay(productUpsReq.getIsDisplay())
                    .status(ProductStatus.ON_SALE)
                    .build();

            productRepository.save(product);

            return product;

        } catch (EntityNotFoundException e) {
            log.error("Category not found with code: " + productUpsReq.getCategoryCode(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found", e);

        } catch (Exception e) {
            log.error("An unexpected error occurred: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", e);
        }
    }

    /**
     * 주어진 상품 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param productUpdReq 업데이트할 상품 정보 요청
     */
    @Transactional
    public void updateProduct(Product product, ProductUpsertRequest productUpdReq) {

        // 변경 사항을 감지하여 엔티티를 저장
        if (updateProductDetails(product, productUpdReq)) {
            productRepository.save(product);
        }
    }

    /**
     * 주어진 상품 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param productUpdReqList 업데이트할 상품 정보 요청 목록
     */
    @Transactional
    public void updateProducts(List<ProductUpsertRequest> productUpdReqList) {

        for (ProductUpsertRequest productUpdReq : productUpdReqList) {
            // ID를 이용해 상품 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Product product = productRepository.findById(productUpdReq.getId())
                    .orElseThrow(() -> new NotFoundException("productNotFound", "주문 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateProductDetails(product, productUpdReq)) {
                productRepository.save(product);
            }
        }
    }

    @Transactional
    public void updateProductStatus(Product product) {
        boolean isSoldOut = product.getProductSizes().stream().allMatch(size -> size.getQuantity() == 0);

        if (isSoldOut) {
            product.setStatus(ProductStatus.OUT_OF_STOCK);
            productRepository.save(product);
        }
    }

    /**
     * 개별 상품 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param product       업데이트할 상품 정보 엔티티
     * @param productUpdReq 업데이트에 사용될 요청 정보
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateProductDetails(Product product, ProductUpsertRequest productUpdReq) {
        boolean isModified = false;

//        // 상품 이름, 설명, 가격, 할인율의 변경 사항을 검사하고 업데이트
//        isModified |= updateIfDifferent(product.getName(), productUpdReq.getName(), product::setName);
//        isModified |= updateIfDifferent(product.getDetail(), productUpdReq.getDetail(), product::setDetail);
//        isModified |= updateIfDifferent(product.getPrice(), productUpdReq.getPrice(), product::setPrice);
//        isModified |= updateIfDifferent(product.getDiscountPer(), productUpdReq.getDiscountPer(), product::setDiscountPer);

        // 상품명 변경 확인 및 검증, 업데이트
        if (!Objects.equals(product.getName(), productUpdReq.getName().trim())) {
            validateName(productUpdReq.getName());
            product.setName(productUpdReq.getName());
            isModified = true;
        }

        // 상품 가격 변경 확인 및 검증, 업데이트
        if (product.getPrice() != productUpdReq.getPrice()) {
            validatePrice(productUpdReq.getPrice());
            product.setPrice(productUpdReq.getPrice());
            isModified = true;
        }

        // 상품 할인율 변경 확인 및 검증, 업데이트
        if (product.getDiscountPer() != productUpdReq.getDiscountPer()) {
            validateDiscountPer(productUpdReq.getDiscountPer());
            product.setDiscountPer(productUpdReq.getDiscountPer());
            isModified = true;
        }

        // 카테고리 변경이 있을 경우 업데이트
        Category categoryAfter = categoryRepository.findByCode(productUpdReq.getCategoryCode())
                .orElseThrow(() -> new NotFoundException("categoryNotFound", "카테고리 정보를 찾을 수 없습니다."));
        if (!product.getCategory().equals(categoryAfter)) {
            product.setCategory(categoryAfter);
            isModified = true;
        }

        // 상품 표시 유무 변경이 있을 경우 업데이트 20240411 추가
        if (product.isDisplay() != productUpdReq.getIsDisplay()) {
            product.setDisplay(productUpdReq.getIsDisplay());
            isModified = true;
        }

        // 상품 상태 변경이 있을 경우 업데이트
        if (product.getStatus() != productUpdReq.getStatus()) {
            product.setStatus(productUpdReq.getStatus());
            isModified = true;
        }

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
     * 현재 값과 새로운 값을 비교하여 다를 경우, 제공된 setter 함수를 사용해 값을 업데이트 (int 타입용)
     *
     * @param currentValue 현재 객체의 필드 값
     * @param newValue     업데이트 할 새로운 값
     * @param setter       현재 값을 업데이트할 setter 메소드 참조
     * @return 값이 변경되었으면 true를, 그렇지 않으면 false를 반환
     */
    private boolean updateIfDifferent(int currentValue, int newValue, IntConsumer setter) {
        if (newValue != currentValue) {
            // 새로운 값이 다르다면 setter 메소드를 사용하여 현재 객체의 값을 업데이트
            setter.accept(newValue);
            return true; // 변경이 있었으므로 true를 반환
        }
        return false; // 값이 변경되지 않았으므로 false를 반환
    }

    /**
     * categoryCode 필드 검증
     */
    private void validateCategoryCode(String categoryCode) {
        if (StringUtils.isBlank(categoryCode)) {
            throw new ValidationException("categoryCodeMissing", "카테고리를 선택해주세요.");
        }
    }

    /**
     * name 필드 검증
     */
    private void validateName(String name) {
        if (StringUtils.isBlank(name)) {
            throw new ValidationException("nameMissing", "상품명을 입력해주세요.");
        } else if (name.trim().length() > MAX_NAME_LENGTH) {
            throw new ValidationException("nameTooLong", "상품명은 " + String.format("%,d", MAX_NAME_LENGTH) + "자 이하로 입력해주세요.");
        }
    }

    /**
     * price 필드 검증
     */
    private void validatePrice(int price) {
        if (price < 1) {
            throw new ValidationException("priceInvalid", "가격은 1 이상의 값이어야 합니다.");
        }
    }

    /**
     * discountPer 필드 검증
     */
    private void validateDiscountPer(int discountPer) {
        if (discountPer < 0 || discountPer >= 100) {
            throw new ValidationException("discountPerInvalid", "할인율은 0 이상 100 미만의 값이어야 합니다.");
        }
    }
}