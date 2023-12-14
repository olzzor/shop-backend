package com.bridgeshop.module.favorite.service;

import com.bridgeshop.module.favorite.dto.FavoriteInfo;
import com.bridgeshop.module.favorite.dto.FavoriteDto;
import com.bridgeshop.module.favorite.entity.Favorite;
import com.bridgeshop.module.favorite.mapper.FavoriteMapper;
import com.bridgeshop.module.favorite.repository.FavoriteRepository;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.dto.ProductSizeDto;
import com.bridgeshop.common.exception.ExistsException;
import com.bridgeshop.common.exception.NotFoundException;
import com.bridgeshop.common.exception.UnavailableException;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.bridgeshop.module.product.entity.ProductStatus;
import com.bridgeshop.module.product.service.ProductService;
import com.bridgeshop.module.product.service.ProductSizeService;
import com.bridgeshop.module.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteMapper favoriteMapper;


    public Favorite retrieveById(Long id) {
        return favoriteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("favoriteNotFound", "관심 상품 정보를 찾을 수 없습니다."));
    }

    public FavoriteInfo checkFavorite(Long userId, Long productSizeId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findByUser_IdAndProductSize_Id(userId, productSizeId);
        if (favoriteOptional.isPresent()) {
            return FavoriteInfo.builder()
                    .id(favoriteOptional.get().getId())
                    .isFavorite(true)
                    .build();
        } else {
            return FavoriteInfo.builder()
                    .isFavorite(false)
                    .build();
        }
    }

    /**
     * 관심 상품 DTO 변환
     */
    public FavoriteDto convertToDto(Favorite favorite) {
        return favoriteMapper.mapToDto(favorite);
    }

    /**
     * 관심 상품 리스트 DTO 변환
     */
    public List<FavoriteDto> convertToDtoList(List<Favorite> favoriteList) {
        return favoriteMapper.mapToDtoList(favoriteList);
    }

    public List<Favorite> getFavoriteList(Long userId) {
        return favoriteRepository.findAllByUser_Id(userId);
    }

    public FavoriteDto getFavoriteDto(Favorite favorite) {
        FavoriteDto favoriteDto = favoriteMapper.mapToDto(favorite);

        ProductDto productDto = productService.getDtoWithMainImage(favorite.getProduct());
        favoriteDto.setProduct(productDto);

        ProductSizeDto productSizeDto = productSizeService.convertToDto(favorite.getProductSize());
        favoriteDto.setProductSize(productSizeDto);

        return favoriteDto;
    }

    public List<FavoriteDto> getFavoriteDtoList(List<Favorite> favoriteList) {
        return favoriteList.stream().map(this::getFavoriteDto).collect(Collectors.toList());
    }

    @Transactional
    public void addFavorite(User user, ProductSize productSize) {
        Product product = productSize.getProduct();

        if (!ProductStatus.ON_SALE.equals(product.getStatus())) {
            // 해당 상품이 판매중이지 않은 경우
            throw new UnavailableException("productNotOnSale", "이 상품은 현재 판매 중이 아닙니다.");

        } else if (productSize.getQuantity() < 1) {
            // 선택 사이즈의 재고가 부족한 경우
            throw new UnavailableException("sizeOutOfStock", "선택한 사이즈의 재고가 없습니다.");

        } else {
            if (favoriteRepository.existsByUserAndProductAndProductSize(user, product, productSize)) {
                throw new ExistsException("favoriteAlreadyExists", "관심상품이 이미 존재합니다.");

            } else {
                Favorite favorite = Favorite.builder()
                        .user(user)
                        .product(product)
                        .productSize(productSize)
                        .build();

                favoriteRepository.save(favorite);
            }
        }
    }

    @Transactional
    public void deleteById(Long favoriteId) {
        favoriteRepository.deleteById(favoriteId);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        favoriteRepository.deleteAllByUserId(userId);
    }
}