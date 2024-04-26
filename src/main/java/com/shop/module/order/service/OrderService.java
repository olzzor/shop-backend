package com.shop.module.order.service;

import com.shop.common.exception.NotFoundException;
import com.shop.module.order.dto.OrderDetailDto;
import com.shop.module.order.dto.OrderDto;
import com.shop.module.order.dto.OrderListSearchRequest;
import com.shop.module.order.dto.OrderPaymentRequest;
import com.shop.module.order.entity.Order;
import com.shop.module.order.entity.OrderDetail;
import com.shop.module.order.entity.OrderStatus;
import com.shop.module.order.mapper.OrderDetailMapper;
import com.shop.module.order.mapper.OrderMapper;
import com.shop.module.order.repository.OrderRepository;
import com.shop.module.product.dto.ProductDto;
import com.shop.module.product.dto.ProductImageDto;
import com.shop.module.product.entity.Product;
import com.shop.module.product.entity.ProductImage;
import com.shop.module.product.entity.ProductSize;
import com.shop.module.product.entity.ProductStatus;
import com.shop.module.product.mapper.ProductImageMapper;
import com.shop.module.product.mapper.ProductMapper;
import com.shop.module.product.mapper.ProductSizeMapper;
import com.shop.module.product.repository.ProductRepository;
import com.shop.module.product.repository.ProductSizeRepository;
import com.shop.module.review.entity.Review;
import com.shop.module.review.mapper.ReviewMapper;
import com.shop.module.shipment.mapper.ShipmentMapper;
import com.shop.module.user.entity.User;
import com.shop.module.user.repository.UserRepository;
import com.siot.IamportRestClient.response.Payment;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductSizeRepository productSizeRepository;
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper;
    private final ProductMapper productMapper;
    private final ProductSizeMapper productSizeMapper;
    private final ProductImageMapper productImageMapper;
    private final ShipmentMapper shipmentMapper;
    private final ReviewMapper reviewMapper;

    public boolean isExistingById(Long id) {
        return orderRepository.existsById(id);
    }

    public Order retrieveById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("orderNotFound", "주문 정보를 찾을 수 없습니다."));
    }

    public Page<Order> retrieveAllPagedByUserId(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserIdOrderByIdDesc(userId, pageable);
    }

    public Page<Order> retrieveAllPaged(Pageable pageable) {
        return orderRepository.findAllByOrderByOrderNumberDesc(pageable);
    }

    public OrderDto convertToDto(Order order) {
        return orderMapper.mapToDto(order);
    }

    public List<OrderDto> convertToDtoList(List<Order> orderList) {
        return orderList.stream()
                .map(this::convertToDto).collect(Collectors.toList());
    }


    public OrderDto getDto(Order order) {
        OrderDto orderDto = orderMapper.mapToDto(order);

        // 20240416 OrderDetial로 이동
//        Review review = order.getReview();
//        if (review != null) {
//            orderDto.setReview(reviewMapper.mapToDto(review));
//        }

        List<OrderDetailDto> orderDetailDtoList = order.getOrderDetails().stream()
                .map(orderDetail -> {
                    OrderDetailDto orderDetailDto = orderDetailMapper.mapToDto(orderDetail);

                    // 상품 정보 설정
                    Product product = orderDetail.getProduct();
                    ProductImage productImage = product.getProductImages().get(0);
                    ProductDto productDto = productMapper.mapToDto(product);
                    ProductImageDto productImageDto = productImageMapper.mapToDto(productImage);
                    productDto.setProductImages(Collections.singletonList(productImageDto)); // ProductDto의 productImages 리스트에 첫 번째 원소로 설정
                    orderDetailDto.setProduct(productDto);

                    // 상품 사이즈 정보 설정
                    orderDetailDto.setProductSize(productSizeMapper.mapToDto(orderDetail.getProductSize()));

                    // 배송 정보 설정
                    orderDetailDto.setShipment(shipmentMapper.mapToDto(orderDetail.getShipment()));

                    // 리뷰 정보가 존재하는 경우에만 리뷰 정보를 설정 20240416 추가
                    if (orderDetail.getReview() != null) {
                        orderDetailDto.setReview(reviewMapper.mapToDto(orderDetail.getReview()));
                    }

                    return orderDetailDto;
                })
                .collect(Collectors.toList());

        orderDto.setOrderDetails(orderDetailDtoList);
        return orderDto;
    }

    public List<OrderDto> getDtoList(List<Order> orderList) {
        return orderList.stream().map(this::getDto).collect(Collectors.toList());
    }

    public Order retrieveByOrderNumber(String orderNumber) {
        return orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new NotFoundException("orderNotFound", "주문 정보를 찾을 수 없습니다."));
    }

    public Long retrieveIdByOrderNumber(String orderNumber) {
        return orderRepository.findIdByOrderNumber(orderNumber);
    }

    public Page<Order> searchAllPaginated(OrderListSearchRequest orderListSearchRequest, Pageable pageable) {
        return orderRepository.findByCondition(orderListSearchRequest, pageable);
    }

    /**
     * 주어진 주문 정보에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param order    업데이트할 주문 정보 엔티티
     * @param orderDto 업데이트에 사용될 DTO
     */
    @Transactional
    public void updateOrder(Order order, OrderDto orderDto) {

        // 변경 사항을 감지하여 엔티티를 저장
        if (updateOrderDetails(order, orderDto)) {
            orderRepository.save(order);
        }
    }

    /**
     * 주어진 주문 정보 목록에 대해 업데이트를 수행
     * 변경이 감지된 경우에만 데이터베이스에 저장
     *
     * @param orderDtoList 업데이트할 주문 정보의 DTO 목록
     */
    @Transactional
    public void updateOrders(List<OrderDto> orderDtoList) {

        for (OrderDto orderDto : orderDtoList) {
            // ID를 이용해 주문 정보 엔티티를 조회하여, 없을 경우 NotFoundException을 발생
            Order order = orderRepository.findById(orderDto.getId())
                    .orElseThrow(() -> new NotFoundException("orderNotFound", "주문 정보를 찾을 수 없습니다."));

            // 변경 사항을 감지하여 엔티티를 저장
            if (updateOrderDetails(order, orderDto)) {
                orderRepository.save(order);
            }
        }
    }

    /**
     * 개별 주문 정보 엔티티에 대한 상세 업데이트를 수행
     * 변경된 필드가 있을 경우에만 업데이트를 진행
     *
     * @param order    업데이트할 주문 정보 엔티티
     * @param orderDto 업데이트에 사용될 DTO
     * @return 변경 사항이 있었으면 true를, 아니면 false를 반환
     */
    private boolean updateOrderDetails(Order order, OrderDto orderDto) {

        boolean isModified = false;

        // 주문자 이메일의 변경 사항을 검사하고 업데이트
        isModified |= updateIfDifferent(order.getBuyerEmail(), orderDto.getBuyerEmail(), order::setBuyerEmail);

        OrderStatus osBefore = order.getStatus(); // 변경 전 주문 상태
        OrderStatus osAfter = orderDto.getStatus(); // 변경 후 주문 상태

        // 주문 상태 변경이 있을 경우 업데이트
        if (osBefore != osAfter) {
            order.setStatus(osAfter);
            isModified = true;
            
            // '주문 취소' -> '주문 완료' 업데이트의 경우
            if (osBefore == OrderStatus.CANCEL_REQUESTED && osAfter == OrderStatus.CANCEL_COMPLETED) {
                for (OrderDetail od : order.getOrderDetails()) {
                    Product product = od.getProduct();
                    ProductSize productSize = od.getProductSize();

                    // 상품의 수량을 복원
                    productSizeRepository.restoreQuantity(productSize.getId(), od.getQuantity());
                    // '일시 품절' 혹은 '품절'의 상품 상태를 판매중으로 복원
                    if (product.getStatus() != ProductStatus.ON_SALE) {
                        product.setStatus(ProductStatus.ON_SALE);
                        productRepository.save(product);
                    }
                }
            }
        }

        if (isModified) {
            orderRepository.save(order);
        }

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

    @Transactional
    public Order insertOrder(Long userId, Payment payment) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        Order order = Order.builder()
                .user(user)
                .orderNumber(payment.getMerchantUid())
                .buyerEmail(payment.getBuyerEmail())
                .paymentMethod(payment.getCardName())
                .paymentAmount(payment.getAmount().intValue())
                .cardNumber(payment.getCardNumber())
                .status(OrderStatus.PAYMENT_COMPLETED) // 20240415 status 수정
                .build();

        return orderRepository.save(order);
    }

    @Transactional
    public Order insertOrderForDirectDeposit(Long userId, OrderPaymentRequest opReq) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("userNotFound", "사용자 정보를 찾을 수 없습니다."));

        Order order = Order.builder()
                .user(user)
                .orderNumber(opReq.getMerchantUid())
                .buyerEmail(opReq.getBuyerEmail())
                .paymentMethod("무통장입금")
                .paymentAmount(Integer.parseInt(opReq.getAmount()))
                .cardNumber("")
                .status(OrderStatus.PAYMENT_PENDING)
                .build();

        return orderRepository.save(order);
    }

    /**
     * 20240415 주문 확정 기능 삭제
    public String confirmOrder(Order order) {
        OrderStatus orderStatus = order.getStatus();
        String message = "";

        if (OrderStatus.ORDER_RECEIVED.equals(orderStatus)
                || OrderStatus.ORDER_CONFIRMED.equals(orderStatus)
                || OrderStatus.SHIPMENT_PREPARING.equals(orderStatus)) {
            // 주문 단계가 '주문 접수', '주문 확인', '배송 준비'의 경우
            updateOrderStatus(order, OrderStatus.ORDER_FINALIZED);
            message = "주문이 확정되었습니다.";

        } else if (OrderStatus.ORDER_FINALIZED.equals(orderStatus)) {
            // 주문 단계가 '주문 확정'의 경우
            message = "이미 주문이 확정되었습니다.";

        } else if (OrderStatus.CANCEL_REQUESTED.equals(orderStatus)) {
            // 주문 단계가 '주문 요청'의 경우
            message = "주문 취소 중에는 주문을 확정할 수 없습니다.";

        } else if (OrderStatus.CANCEL_COMPLETED.equals(orderStatus)) {
            // 주문 단계가 '취소 완료'의 경우
            message = "취소된 주문은 주문을 확정할 수 없습니다.";
        }

        return message;
    }
    */

    public String cancelOrder(Order order) {
        OrderStatus orderStatus = order.getStatus();
        String message = "";

        if (OrderStatus.PAYMENT_PENDING.equals(orderStatus) || OrderStatus.PAYMENT_COMPLETED.equals(orderStatus)) {
            updateOrderStatus(order, OrderStatus.CANCEL_REQUESTED);
            message = "주문 취소가 접수되었습니다.";

//         TODO ShipmentStatus 체크 처리 추가할 것
//        } else if (OrderStatus.SHIPMENT_PREPARING.equals(orderStatus)) {
//            message = "배송 준비 중에는 주문을 취소할 수 없습니다.";

        } else if (OrderStatus.CANCEL_REQUESTED.equals(orderStatus)) {
            message = "이미 주문 취소가 접수되었습니다. 잠시 기다려주세요.";

        } else if (OrderStatus.CANCEL_COMPLETED.equals(orderStatus)) {
            message = "이미 주문 취소가 완료되었습니다.";
        }

        return message;
    }

    @Transactional
    public Order updateOrderStatus(Order order, OrderStatus orderStatus) {
        order.setStatus(orderStatus);
        return orderRepository.save(order);
    }
}