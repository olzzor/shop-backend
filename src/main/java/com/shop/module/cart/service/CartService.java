package com.shop.module.cart.service;

import com.shop.common.exception.NotFoundException;
import com.shop.common.exception.UnavailableException;
import com.shop.module.cart.dto.CartProductDto;
import com.shop.module.cart.entity.Cart;
import com.shop.module.cart.entity.CartProduct;
import com.shop.module.cart.repository.CartProductRepository;
import com.shop.module.cart.repository.CartRepository;
import com.shop.module.wishlist.entity.Wishlist;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.product.entity.ProductStatus;
import com.shop.module.user.entity.User;
import com.shop.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartProductService cartProductService;

    private final CartRepository cartRepository;
    private final CartProductRepository cartProductRepository;
    private final UserRepository userRepository;

    public Cart retrieveByUserId(Long userId) {
        return cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("cartNotFound", "장바구니 정보를 찾을 수 없습니다."));
    }

    public List<CartProductDto> getProductsInCart(Long userId) {
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new NotFoundException("cartNotFound", "장바구니 정보를 찾을 수 없습니다."));

        List<CartProduct> cartProductList = cart.getCartProducts();
        List<CartProductDto> cartProductDtoList = cartProductService.getDtoList(cartProductList);

        return cartProductDtoList;
    }

    @Transactional
    public void addWishlistToCart(Long userId, Wishlist wishlist) {
        Product product = wishlist.getProduct();
        ProductSize productSize = wishlist.getProductSize();

        if (!ProductStatus.ON_SALE.equals(product.getStatus())) {
            // 해당 상품이 판매 중이지 않은 경우{
            throw new UnavailableException("productNotOnSale", "이 상품은 현재 판매 중이 아닙니다.");

        } else if (productSize.getQuantity() < 1) {
            // 선택 사이즈의 재고가 부족한 경우
            throw new UnavailableException("sizeOutOfStock", "선택한 사이즈의 재고가 없습니다.");

        } else {
            // 사용자의 장바구니 가져오기 (또는 생성)
            Optional<Cart> cartOptional = cartRepository.findByUser_Id(userId);
            Cart cart;

            if (cartOptional.isPresent()) {
                cart = cartOptional.get();

            } else {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

                cart = Cart.builder()
                        .user(user)
                        .isActivate(true)
                        .build();

                cartRepository.save(cart);
            }

            Optional<CartProduct> cartProductOptional = cartProductRepository.findByCartAndProductAndProductSize(cart, product, productSize);
            CartProduct cartProduct;

            if (cartProductOptional.isPresent()) {
                cartProduct = cartProductOptional.get();
                if (productSize.getQuantity() > cartProduct.getQuantity()) {
                    cartProduct.setQuantity(cartProduct.getQuantity() + 1);
                } else {
                    throw new UnavailableException("cartOutOfStock", "장바구니에 추가할 수 없습니다. 재고가 부족합니다.");
                }

            } else {
                cartProduct = CartProduct.builder()
                        .cart(cart)
                        .product(product)
                        .productSize(productSize)
                        .quantity(1)
                        .build();
            }

            cartProductRepository.save(cartProduct);
        }
    }

    @Transactional
    public void addToCart(Long userId, ProductSize productSize, int quantity) {
        Product product = productSize.getProduct();

        if (!ProductStatus.ON_SALE.equals(product.getStatus())) {
            // 해당 상품이 판매 중이지 않은 경우{
            throw new UnavailableException("productNotOnSale", "이 상품은 현재 판매 중이 아닙니다.");

        } else if (productSize.getQuantity() < quantity) {
            // 선택 사이즈의 재고가 부족한 경우
            throw new UnavailableException("sizeOutOfStock", "선택한 사이즈의 재고가 없습니다.");

        } else {
            // 사용자의 장바구니 가져오기 (또는 생성)
            Optional<Cart> cartOptional = cartRepository.findByUser_Id(userId);
            Cart cart;

            if (cartOptional.isPresent()) {
                cart = cartOptional.get();

            } else {
                User user = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

                cart = Cart.builder()
                        .user(user)
                        .isActivate(true)
                        .build();

                cartRepository.save(cart);
            }

            Optional<CartProduct> cartProductOptional = cartProductRepository.findByCartAndProductAndProductSize(cart, product, productSize);
            CartProduct cartProduct;

            if (cartProductOptional.isPresent()) {
                cartProduct = cartProductOptional.get();
                if (productSize.getQuantity() < cartProduct.getQuantity() + quantity) {
                    throw new UnavailableException("cartOutOfStock", "장바구니에 추가할 수 없습니다. 재고가 부족합니다.");
                } else {
                    cartProduct.setQuantity(cartProduct.getQuantity() + quantity);
                }

            } else {
                cartProduct = CartProduct.builder()
                        .cart(cart)
                        .product(product)
                        .productSize(productSize)
                        .quantity(quantity)
                        .build();
            }

            cartProductRepository.save(cartProduct);
        }
    }

    @Transactional
    public void createCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        Cart cart = Cart.builder()
                .user(user)
                .isActivate(true)
                .build();

        cartRepository.save(cart);
    }

    @Transactional
    public void deactivateCart(Long userId) {
        Cart cart = retrieveByUserId(userId);
        cart.setActivate(false);
        cartRepository.save(cart);
    }
}