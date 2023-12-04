package com.bridgeshop.module.recentlyviewedproduct.service;

import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.service.ProductService;
import com.bridgeshop.module.recentlyviewedproduct.dto.RecentlyViewedProductDto;
import com.bridgeshop.module.recentlyviewedproduct.dto.RecentlyViewedProductRequest;
import com.bridgeshop.module.recentlyviewedproduct.repository.RecentlyViewedProductRepository;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.recentlyviewedproduct.entity.RecentlyViewedProduct;
import com.bridgeshop.module.recentlyviewedproduct.mapper.RecentlyViewedProductMapper;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.user.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RecentlyViewedProductService {

    private final UserService userService;
    private final ProductService productService;
    private final RecentlyViewedProductRepository recentlyViewedProductRepository;
    private final RecentlyViewedProductMapper recentlyViewedProductMapper;

    private static final int MAX_RECENTLY_VIEWED_PRODUCTS = 20;

    public Page<RecentlyViewedProduct> getRecentlyViewedProductPage(Long userId, Pageable pageable) {
        return recentlyViewedProductRepository.findAllByUser_IdOrderByViewedAtDesc(userId, pageable);
    }

//    public List<RecentlyViewedProduct> getRecentlyViewedProductList(Long userId) {
//        return recentlyViewedProductRepository.findAllByUser_IdOrderByViewedAtDesc(userId);
//    }

    public RecentlyViewedProductDto getRecentlyViewedProductDto(RecentlyViewedProduct recentlyViewedProduct) {
        RecentlyViewedProductDto recentlyViewedProductDto = recentlyViewedProductMapper.mapToDto(recentlyViewedProduct);

        ProductDto productDto = productService.getDtoWithMainImage(recentlyViewedProduct.getProduct());
        recentlyViewedProductDto.setProduct(productDto);

        return recentlyViewedProductDto;
    }

    public List<RecentlyViewedProductDto> getRecentlyViewedProductDtoList(List<RecentlyViewedProduct> recentlyViewedProductList) {
        return recentlyViewedProductList.stream()
                .map(this::getRecentlyViewedProductDto).collect(Collectors.toList());
    }

    @Transactional
    public void recordRecentlyViewedProduct(Long userId, Long productId) {
        upsertRecentlyViewedProduct(userId, productId, LocalDateTime.now());
    }

    @Transactional
    public void syncRecentlyViewedProduct(Long userId, RecentlyViewedProductRequest recentlyViewedProductRequest) {
        Long productId = recentlyViewedProductRequest.getProductId();
        LocalDateTime viewedAt = LocalDateTime.parse(recentlyViewedProductRequest.getViewedAt());

        upsertRecentlyViewedProduct(userId, productId, viewedAt);
    }

    private void upsertRecentlyViewedProduct(Long userId, Long productId, LocalDateTime viwedAt) {
        // RecentView 데이터가 존재하는지 확인
        Optional<RecentlyViewedProduct> rvpOptional = recentlyViewedProductRepository.findByUser_IdAndProduct_Id(userId, productId);

        if (rvpOptional.isPresent()) {
            // 존재하는 경우, viewedAt 날짜 갱신
            RecentlyViewedProduct rvp = rvpOptional.get();
            rvp.setViewedAt(viwedAt);
            recentlyViewedProductRepository.save(rvp);

        } else {
            // 존재하지 않는 경우, 새로운 RecentView 데이터 추가
            User user = userService.retrieveById(userId);
            Product product = productService.retrieveById(productId);

            RecentlyViewedProduct rvp = new RecentlyViewedProduct();
            rvp.setUser(user);
            rvp.setProduct(product);
            rvp.setViewedAt(viwedAt);
            recentlyViewedProductRepository.save(rvp);

            // 해당 유저의 RecentView 데이터가 제한을 넘는다면, 가장 오래된 데이터 삭제
            List<RecentlyViewedProduct> rvpList = recentlyViewedProductRepository.findAllByUser_IdOrderByViewedAtDesc(userId);
            if (rvpList.size() >= MAX_RECENTLY_VIEWED_PRODUCTS) {
                recentlyViewedProductRepository.delete(rvpList.get(MAX_RECENTLY_VIEWED_PRODUCTS - 1));
            }
        }
    }
}