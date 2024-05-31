package com.shop.module.recommendedproduct.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.ValidationException;
import com.shop.module.category.dto.CategoryDto;
import com.shop.module.category.mapper.CategoryMapper;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductImageDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.entity.Product;
import com.shop.module.product.mapper.ProductImageMapper;
import com.shop.module.product.mapper.ProductMapper;
import com.shop.module.product.mapper.ProductSizeMapper;
import com.shop.module.product.repository.ProductRepository;
import com.shop.module.recommendedproduct.dto.RecommendedProductDto;
import com.shop.module.recommendedproduct.dto.RecommendedProductUpsertRequest;
import com.shop.module.recommendedproduct.entity.RecommendedProduct;
import com.shop.module.recommendedproduct.mapper.RecommendedProductMapper;
import com.shop.module.recommendedproduct.repository.RecommendedProductRepository;
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
public class RecommendedProductService {

    private final RecommendedProductRepository recommendedProductRepository;
    private final ProductRepository productRepository;

    private final RecommendedProductMapper recommendedProductMapper;
    private final ProductMapper productMapper;
    private final ProductSizeMapper productSizeMapper;
    private final ProductImageMapper productImageMapper;
    private final CategoryMapper categoryMapper;

    private static final int MAX_RECOMMENDED_PRODUCTS = 8;

    public List<RecommendedProduct> retrieveAll() {
        return recommendedProductRepository.findAllByOrderByDisplayOrder();
    }

    public Page<RecommendedProduct> retrieveAllPaged(Pageable pageable) {
        return recommendedProductRepository.findAllByOrderByDisplayOrder(pageable);
    }

    // TODO 페이징 방법 수정 필요
//    public Page<RecommendedProduct> getAllPaged(Pageable pageable) {

        /**
        int pageNumber = pageable.getPageNumber();
        long totalProducts = recommendedProductRepository.count();

        int pageSize = 4; // 페이지 당 상품 수
        int start = (pageNumber == 0) ? 0 : 2 + 2 * (pageNumber - 1);

        if (start >= totalProducts) {
            return Page.empty(); // 시작점이 총 상품 수를 초과하는 경우 빈 페이지 반환
        }

        int fetchSize = Math.min(pageSize, (int) (totalProducts - start));
        int actualPageNumber = start / pageSize; // 실제 페이지 번호 계산

        return recommendedProductRepository.findAllByOrderByDisplayOrder(PageRequest.of(actualPageNumber, fetchSize, pageable.getSort()));
        */

        /**
        int pageNumber = pageable.getPageNumber();
        long totalProducts = recommendedProductRepository.count();
        List<RecommendedProduct> rpList = new ArrayList<>();

        if (pageNumber == 0) {
            // 첫번째 페이지: 4개 상품 로드
            return recommendedProductRepository.findAllByOrderByDisplayOrder(PageRequest.of(0, 4, pageable.getSort()));

        } else {
            // 두번째 페이지 이후: 이전 페이지의 마지막 2개 상품 + 2개 상품 추가 로드
            int start = (pageNumber - 1) * 2 + 2;
            int pageSize = Math.min(4, (int) (totalProducts - start)); // pageSize는 4로 고정하되, 남은 상품 수가 4보다 적을 경우 남은 상품 수만큼 로드
            return recommendedProductRepository.findAllByOrderByDisplayOrder(PageRequest.of(pageNumber, pageSize, pageable.getSort()));
        }
        */
//    }

    public RecommendedProductDto getDto(RecommendedProduct recommendedProduct) {
        RecommendedProductDto recommendedProductDto = recommendedProductMapper.mapToDto(recommendedProduct);

        Product product = recommendedProduct.getProduct();
        ProductDto productDto = productMapper.mapToDto(product);

        // 상품의 이미지 파일 중 displayOrder가 1인 파일만 필터링하여 DTO 변환
        List<ProductImageDto> productImageDtoList = product.getProductImages().stream()
                .filter(productImage -> productImage.getDisplayOrder() == 1) // displayOrder가 1인 ProductImage만 필터링
                .map(productImageMapper::mapToDto) // 필터링된 ProductImage을 ProductImageDto로 매핑
                .collect(Collectors.toList()); // 결과를 리스트로 수집

        // 변환된 DTO 리스트를 productDto에 설정
        productDto.setProductImages(productImageDtoList);

        recommendedProductDto.setProduct(productDto);
        return recommendedProductDto;
    }

    public RecommendedProductDto getDtoDetail(RecommendedProduct recommendedProduct) {
        RecommendedProductDto recommendedProductDto = recommendedProductMapper.mapToDto(recommendedProduct);

        // 상품 정보 취득
        Product product = recommendedProduct.getProduct();
        ProductDto productDto = productMapper.mapToDto(product);

        // 상품의 카테고리 정보 취득 및 설정
        CategoryDto categoryDto = categoryMapper.mapToDto(product.getCategory());
        productDto.setCategory(categoryDto);

        // 상품의 사이즈 리스트 취득 및 설정
        List<ProductSizeDto> productSizeDtoList = productSizeMapper.mapToDtoList(product.getProductSizes());
        productDto.setProductSizes(productSizeDtoList);

        // 상품의 이미지 파일 중 displayOrder가 1인 파일만 필터링하여 DTO 변환
        List<ProductImageDto> productImageDtoList = product.getProductImages().stream()
                .filter(productImage -> productImage.getDisplayOrder() == 1) // displayOrder가 1인 ProductImage만 필터링
                .map(productImageMapper::mapToDto) // 필터링된 ProductImage을 ProductImageDto로 매핑
                .collect(Collectors.toList()); // 결과를 리스트로 수집

        // 변환된 DTO 리스트를 productDto에 설정
        productDto.setProductImages(productImageDtoList);

        recommendedProductDto.setProduct(productDto);
        return recommendedProductDto;
    }

    public List<RecommendedProductDto> getDtoList(List<RecommendedProduct> recommendedProductList) {
        return recommendedProductList.stream()
                .map(this::getDto).collect(Collectors.toList());
    }

    public List<RecommendedProductDto> getDtoListDetail(List<RecommendedProduct> recommendedProductList) {
        return recommendedProductList.stream()
                .map(this::getDtoDetail).collect(Collectors.toList());
    }

    public void insertRecommendedProduct(RecommendedProductUpsertRequest rpInsReq) {
        Product product = productRepository.findById(rpInsReq.getProductId())
                .orElseThrow(() -> new NotFoundException("productNotFound", "상품 정보를 찾을 수 없습니다."));

        RecommendedProduct rp = RecommendedProduct.builder()
                .product(product)
                .displayOrder(rpInsReq.getDisplayOrder())
                .build();

        recommendedProductRepository.save(rp);
    }

    @Transactional
    public void insertRecommendedProducts(List<RecommendedProductUpsertRequest> rpInsReqList) {
        int existingCount = recommendedProductRepository.countAllBy();

        if (existingCount + rpInsReqList.size() > MAX_RECOMMENDED_PRODUCTS) {
            throw new ValidationException("recommendedProductLimitReached", "최대 추천 상품 수를 초과하였습니다.");
        }

        for (RecommendedProductUpsertRequest rpInsReq : rpInsReqList) {
            insertRecommendedProduct(rpInsReq);
        }
    }

    @Transactional
    public void deleteById(Long recommendedProductId) {
//        recommendedProductRepository.deleteById(recommendedProductId);

        // 삭제할 추천 상품의 displayOrder를 추출
        int deletedDisplayOrder = recommendedProductRepository
                .findDisplayOrderById(recommendedProductId)
                .orElseThrow(() -> new NotFoundException("recommendedProductNotFound", "추천 상품 정보를 찾을 수 없습니다."));

        // 추천 상품 삭제
        recommendedProductRepository.deleteById(recommendedProductId);

        // 삭제된 displayOrder 이후의 추천 상품 목록을 가져와서 displayOrder를 재정렬
        List<RecommendedProduct> subsequentProducts = recommendedProductRepository
                .findByDisplayOrderGreaterThan(deletedDisplayOrder);

        subsequentProducts.forEach(product -> {
            product.setDisplayOrder(product.getDisplayOrder() - 1);
            recommendedProductRepository.save(product);
        });
    }

    /**
     * 개별 추천 상품 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param rp       업데이트할 추천 상품 정보 엔티티
     * @param rpUpdReq 업데이트에 사용될 요청 정보
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateRecommendedProductDetails(RecommendedProduct rp, RecommendedProductUpsertRequest rpUpdReq) {
        boolean isModified = false;

        // 추천 상품 출력 순서 변경 확인 및 업데이트
        if (rp.getDisplayOrder() != rpUpdReq.getDisplayOrder()) {
            rp.setDisplayOrder(rpUpdReq.getDisplayOrder());
            isModified = true;
        }

        return isModified;
    }

    @Transactional
    public void updateRecommendedProducts(List<RecommendedProductUpsertRequest> rpUpdReqList) {

        for (RecommendedProductUpsertRequest rpUpdReq : rpUpdReqList) {
            RecommendedProduct rp = recommendedProductRepository.findById(rpUpdReq.getId())
                    .orElseThrow(() -> new NotFoundException("couponNotFound", "쿠폰 정보를 찾을 수 없습니다."));

            if (updateRecommendedProductDetails(rp, rpUpdReq)) {
                recommendedProductRepository.save(rp);
            }
        }
    }
}