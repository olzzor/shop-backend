package com.shop.module.cart.service;

import com.shop.common.exception.NotFoundException;
import com.shop.module.cart.dto.CartProductDto;
import com.shop.module.cart.entity.CartProduct;
import com.shop.module.cart.mapper.CartProductMapper;
import com.shop.module.cart.repository.CartProductRepository;
import com.shop.module.cart.repository.CartRepository;
import com.shop.module.category.dto.CategoryDto;
import com.shop.module.category.service.CategoryService;
import com.shop.module.coupon.dto.CouponDto;
import com.shop.module.coupon.entity.Coupon;
import com.shop.module.coupon.repository.CouponRepository;
import com.shop.module.coupon.service.CouponService;
import com.shop.module.wishlist.repository.WishlistRepository;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductSizeDto;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.product.mapper.ProductSizeMapper;
import com.shop.module.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartProductService {

    private final ProductService productService;
    private final CouponService couponService;
    private final CategoryService categoryService;
    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final CouponRepository couponRepository;
    private final CartProductMapper cartProductMapper;
    private final ProductSizeMapper productSizeMapper;


    public CartProduct retrieveById(Long id) {
        return cartProductRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("cartProductNotFound", "장바구니 상품 정보를 찾을 수 없습니다."));
    }

    public CartProductDto getDto(CartProduct cartProduct) {
        Product product = cartProduct.getProduct();
        ProductSize productSize = cartProduct.getProductSize();

        CartProductDto cartProductDto = cartProductMapper.mapToDto(cartProduct);

        ProductDto productDto = productService.getDtoWithMainImage(product);
        CategoryDto categoryDto = categoryService.convertToDto(product.getCategory());
        productDto.setCategory(categoryDto);
        ProductSizeDto productSizeDto = productSizeMapper.mapToDto(productSize);

        cartProductDto.setProduct(productDto);
        cartProductDto.setProductSize(productSizeDto);

        if (cartProduct.getCoupon() != null) {
            CouponDto couponDto = couponService.convertToDto(cartProduct.getCoupon());
            cartProductDto.setCoupon(couponDto);
        }

        return cartProductDto;
    }

    public List<CartProductDto> getDtoList(List<CartProduct> cartProductList) {
        return cartProductList.stream().map(this::getDto).collect(Collectors.toList());
    }

    public boolean checkStock(Long userId) {
        Long cartId = cartRepository.findIdByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("cartNotFound", "장바구니 정보를 찾을 수 없습니다."));

        List<CartProduct> cartProductList = cartProductRepository.findAllByCart_Id(cartId);

        if (cartProductList.isEmpty()) {
            throw new NotFoundException("cartProductEmpty", "장바구니에 상품이 없습니다.");
        }

        for (CartProduct cartProduct : cartProductList) {
            if (cartProduct.getQuantity() > cartProduct.getProductSize().getQuantity()) {
                return false; // 재고가 부족한 경우
            }
        }
        return true; // 모든 상품에 대해 재고가 충분한 경우
    }

    private boolean isCouponApplicable(CartProduct cartProduct, Coupon coupon) {
        // 현재 날짜가 쿠폰의 유효기간 내에 있는지 확인
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(coupon.getStartValidDate()) || now.isAfter(coupon.getEndValidDate())) {
            return false;
        }

        // 쿠폰 적용을 위한 최소 구매 금액 조건 확인
        if (cartProduct.getProduct().getPrice() < coupon.getMinAmount()) {
            return false;
        }

        // 쿠폰이 특정 카테고리에만 적용 가능한 경우, 해당 상품의 카테고리 확인
        if (!coupon.getCouponCategories().isEmpty() &&
                coupon.getCouponCategories().stream().noneMatch(cc -> cc.getCategory().getId().equals(cartProduct.getProduct().getCategory().getId()))) {
            return false;
        }

        // 쿠폰이 특정 상품에만 적용 가능한 경우, 해당 상품 확인
        if (!coupon.getCouponProducts().isEmpty() &&
                coupon.getCouponProducts().stream().noneMatch(cp -> cp.getProduct().getId().equals(cartProduct.getProduct().getId()))) {
            return false;
        }

        return true;
    }

    @Transactional
    public void updateProductQuantity(Long cartId, Long productId, int quantity) {
        CartProduct cartProduct = cartProductRepository.findByCart_IdAndProduct_Id(cartId, productId)
                .orElseThrow(() -> new NotFoundException("cartProductNotFound", "장바구니 상품 정보를 찾을 수 없습니다."));
        cartProduct.setQuantity(quantity);
        cartProductRepository.save(cartProduct);
    }

//    @Transactional
//    public void updateProductCoupon(Long cartProductId, Long couponId) {
//        CartProduct cartProduct = cartProductRepository.findById(cartProductId)
//                .orElseThrow(() -> new NotFoundException("cartProductNotFound", "장바구니 상품 정보를 찾을 수 없습니다."));
//        Coupon coupon = couponRepository.findById(couponId)
//                .orElseThrow(() -> new NotFoundException("couponNotFound", "쿠폰 정보를 찾을 수 없습니다."));
//        cartProduct.setCoupon(coupon);
//        cartProductRepository.save(cartProduct);
//    }

    @Transactional
    public void updateProductCoupon(Long userId, Long couponId) {
        List<CartProduct> cartProductList = cartProductRepository.findAllByCart_User_Id(userId);

        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new NotFoundException("couponNotFound", "쿠폰 정보를 찾을 수 없습니다."));

        for (CartProduct cartProduct : cartProductList) {
            // 쿠폰 적용 가능 여부를 검사하는 로직 구현 필요
            if (isCouponApplicable(cartProduct, coupon)) {
                cartProduct.setCoupon(coupon); // 쿠폰 적용
                cartProductRepository.save(cartProduct);
            }
        }
    }

    @Transactional
    public void deleteById(Long cartProductId) {
        cartProductRepository.deleteById(cartProductId);
    }

    @Transactional
    public void deleteByCartId(Long cartId) {
        cartProductRepository.deleteByCart_Id(cartId);
    }
}