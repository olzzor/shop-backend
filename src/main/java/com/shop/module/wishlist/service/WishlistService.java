package com.shop.module.wishlist.service;

import com.shop.common.exception.ExistsException;
import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.UnavailableException;
import com.shop.module.wishlist.dto.WishlistDto;
import com.shop.module.wishlist.dto.WishlistInfo;
import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.wishlist.mapper.WishlistMapper;
import com.shop.module.wishlist.repository.WishlistRepository;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.product.entity.ProductStatus;
import com.shop.module.product.service.ProductService;
import com.shop.module.product.service.ProductSizeService;
import com.shop.module.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishlistService {

    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final WishlistRepository wishlistRepository;
    private final WishlistMapper wishlistMapper;


    public Wishlist retrieveById(Long id) {
        return wishlistRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("wishlistNotFound", "위시리스트 정보를 찾을 수 없습니다."));
    }

    public WishlistInfo checkWishlist(Long userId, Long productSizeId) {
        Optional<Wishlist> wishlistOptional = wishlistRepository.findByUser_IdAndProductSize_Id(userId, productSizeId);
        if (wishlistOptional.isPresent()) {
            return WishlistInfo.builder()
                    .id(wishlistOptional.get().getId())
                    .isWishlist(true)
                    .build();
        } else {
            return WishlistInfo.builder()
                    .isWishlist(false)
                    .build();
        }
    }

    /**
     * 관심 상품 DTO 변환
     */
    public WishlistDto convertToDto(Wishlist wishlist) {
        return wishlistMapper.mapToDto(wishlist);
    }

    /**
     * 관심 상품 리스트 DTO 변환
     */
    public List<WishlistDto> convertToDtoList(List<Wishlist> wishlistList) {
        return wishlistMapper.mapToDtoList(wishlistList);
    }

    public List<Wishlist> getWishlistList(Long userId) {
        return wishlistRepository.findAllByUser_Id(userId);
    }

    public WishlistDto getWishlistDto(Wishlist wishlist) {
        WishlistDto wishlistDto = wishlistMapper.mapToDto(wishlist);

        ProductDto productDto = productService.getDtoWithMainImage(wishlist.getProduct());
        wishlistDto.setProduct(productDto);

        ProductSizeDto productSizeDto = productSizeService.convertToDto(wishlist.getProductSize());
        wishlistDto.setProductSize(productSizeDto);

        return wishlistDto;
    }

    public List<WishlistDto> getWishlistDtoList(List<Wishlist> wishlistList) {
        return wishlistList.stream().map(this::getWishlistDto).collect(Collectors.toList());
    }

    @Transactional
    public void addToWishlist(User user, ProductSize productSize) {
        Product product = productSize.getProduct();

        if (!ProductStatus.ON_SALE.equals(product.getStatus())) {
            // 해당 상품이 판매중이지 않은 경우
            throw new UnavailableException("productNotOnSale", "이 상품은 현재 판매 중이 아닙니다.");

        } else if (productSize.getQuantity() < 1) {
            // 선택 사이즈의 재고가 부족한 경우
            throw new UnavailableException("sizeOutOfStock", "선택한 사이즈의 재고가 없습니다.");

        } else {
            if (wishlistRepository.existsByUserAndProductAndProductSize(user, product, productSize)) {
                throw new ExistsException("wishlistAlreadyExists", "상품이 이미 위시리스트에 존재합니다.");

            } else {
                Wishlist wishlist = Wishlist.builder()
                        .user(user)
                        .product(product)
                        .productSize(productSize)
                        .build();

                wishlistRepository.save(wishlist);
            }
        }
    }

    @Transactional
    public void deleteById(Long wishlistId) {
        wishlistRepository.deleteById(wishlistId);
    }

    @Transactional
    public void deleteAllByUserId(Long userId) {
        wishlistRepository.deleteAllByUserId(userId);
    }
}