package com.bridgeshop.module.order.service;

import com.bridgeshop.module.cart.entity.Cart;
import com.bridgeshop.module.cart.entity.CartProduct;
import com.bridgeshop.module.coupon.entity.Coupon;
import com.bridgeshop.module.coupon.entity.CouponDiscountType;
import com.bridgeshop.module.order.dto.OrderDetailDto;
import com.bridgeshop.module.order.entity.OrderDetail;
import com.bridgeshop.module.order.repository.OrderDetailRepository;
import com.bridgeshop.module.product.dto.ProductDto;
import com.bridgeshop.module.product.service.ProductService;
import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.order.mapper.OrderDetailMapper;
import com.bridgeshop.module.shipment.dto.ShipmentDto;
import com.bridgeshop.module.product.entity.Product;
import com.bridgeshop.module.product.entity.ProductSize;
import com.bridgeshop.module.cart.repository.CartRepository;
import com.bridgeshop.module.product.service.ProductSizeService;
import com.bridgeshop.module.shipment.entity.Shipment;
import com.bridgeshop.module.shipment.service.ShipmentService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final ProductService productService;
    private final ProductSizeService productSizeService;
    private final ShipmentService shipmentService;
    private final CartRepository cartRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailMapper orderDetailMapper;

    public boolean isExistingById(Long id) {
        return orderDetailRepository.existsById(id);
    }

    public OrderDetail retrieveById(Long id) {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("OrderDetail not found"));
    }

    public List<OrderDetail> retrieveAllByOrderId(Long orderId) {
        return orderDetailRepository.findAllByOrder_id(orderId);
    }

    public OrderDetailDto convertToDto(OrderDetail orderDetail) {
        return orderDetailMapper.mapToDto(orderDetail);
    }

    public List<OrderDetailDto> convertToDtoList(List<OrderDetail> orderDetailList) {
        return orderDetailList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 주문 상세 표시용
    public OrderDetailDto getOrderDetail(OrderDetail orderDetail) {
        OrderDetailDto orderDetailDto = convertToDto(orderDetail);

        ProductDto productDto = productService.getDtoWithMainImage(orderDetail.getProduct());
        orderDetailDto.setProduct(productDto);

        ShipmentDto shipmentDto = shipmentService.getShipmentDto(orderDetail.getShipment());
        orderDetailDto.setShipment(shipmentDto);

        return orderDetailDto;
    }

    public List<OrderDetailDto> getOrderDetailListByOrderId(Long orderId) {
        List<OrderDetail> orderDetailList = orderDetailRepository.findAllByOrder_id(orderId);

        return orderDetailList.stream()
                .map(this::getOrderDetail)
                .collect(Collectors.toList());
    }

    @Transactional
    public void insertOrderDetail(Long userId, Order order, Shipment shipment) {

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        List<CartProduct> cartProductList = cart.getCartProducts();

        List<OrderDetail> orderDetailList = new ArrayList<>();

        for (CartProduct cartProduct : cartProductList) {
            Product product = cartProduct.getProduct();
            ProductSize productSize = cartProduct.getProductSize();
            Coupon coupon = cartProduct.getCoupon();
            int quantity = cartProduct.getQuantity();

            productSizeService.decreaseProductSizeQuantity(productSize, quantity);
            productService.updateProductStatus(product);

            OrderDetail orderDetail = new OrderDetail();

            orderDetail.setOrder(order);
            orderDetail.setProduct(product);
            orderDetail.setProductSize(productSize);
            orderDetail.setCoupon(coupon);
            orderDetail.setQuantity(quantity);
            orderDetail.setUnitPrice(product.getPrice());
            orderDetail.setDiscountPer(product.getDiscountPer());
            orderDetail.setFinalPrice(getFinalPrice(product, coupon));
            orderDetail.setShipment(shipment);

            orderDetailList.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetailList);
    }

    /**
     * 주문 시점의 최종 가격을 계산하는 메소드.
     * 상품의 원래 가격에서 상품의 할인율을 적용한 가격을 계산하고,
     * 사용 가능한 쿠폰이 있으면 쿠폰에 따른 추가 할인을 적용합니다.
     *
     * @param product 주문할 상품
     * @param coupon  사용할 쿠폰 (없을 경우 null)
     * @return 최종 계산된 가격 (할인 및 쿠폰 적용 후)
     */
    private int getFinalPrice(Product product, Coupon coupon) {
        int originPrice = product.getPrice();
        int discountPrice = originPrice * product.getDiscountPer() / 100;
        int discountedPrice = originPrice - discountPrice;
        int couponPrice = 0;

        if (coupon != null && discountedPrice >= coupon.getMinAmount()) {
            if (coupon.getDiscountType() == CouponDiscountType.PERCENTAGE_DISCOUNT) {
                // 퍼센트 할인 적용
                couponPrice = discountedPrice * coupon.getDiscountValue() / 100;
            } else if (coupon.getDiscountType() == CouponDiscountType.AMOUNT_DISCOUNT) {
                // 금액 할인 적용
                couponPrice = coupon.getDiscountValue();
            }
        }

        // 최종 가격 계산 (최종 가격이 0보다 작아지지 않도록 함)
        return Math.max(discountedPrice - couponPrice, 0);
    }
}