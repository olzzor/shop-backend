package com.bridgeshop.module.order.controller;

import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.module.cart.service.CartProductService;
import com.bridgeshop.module.order.dto.OrderDetailDto;
import com.bridgeshop.module.order.dto.OrderDto;
import com.bridgeshop.module.order.dto.OrderListResponse;
import com.bridgeshop.module.order.dto.OrderListSearchRequest;
import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.order.service.OrderService;
import com.bridgeshop.module.shipment.dto.ShipmentDto;
import com.bridgeshop.module.shipment.entity.Shipment;
import com.bridgeshop.module.shipment.service.ShipmentService;
import com.bridgeshop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/order")
public class OrderController {

    private final JwtService jwtService;
    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final CartProductService cartProductService;

    @GetMapping("/check-stock")
    public ResponseEntity<?> checkStock(@CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(cartProductService.checkStock(jwtService.getId(token)), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity getOrderHistory(@CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res,
                                          Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        Page<Order> orderPage = orderService.retrieveAllPagedByUserId(jwtService.getId(token), pageable);
        List<OrderDto> orderDtoList = orderService.getDtoList(orderPage.getContent());

        OrderListResponse orderListResponse = OrderListResponse.builder()
                .orders(orderDtoList)
                .totalPages(orderPage.getTotalPages())
                .build();

        return new ResponseEntity<>(orderListResponse, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity getOrderList(@CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res,
                                       Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Order> orderPage = orderService.retrieveAllPaged(pageable);
            List<OrderDto> orderDtoList = orderService.getDtoList(orderPage.getContent());

            OrderListResponse orderListResponse = OrderListResponse.builder()
                    .orders(orderDtoList)
                    .totalPages(orderPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(orderListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/detail/admin/{id}")
    public ResponseEntity getOrderDetailAdimin(@PathVariable("id") Long orderId,
                                               @CookieValue(value = "token", required = false) String accessToken,
                                               @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                               HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Order order = orderService.retrieveById(orderId);
            OrderDto orderDto = orderService.getDto(order);

            return new ResponseEntity<>(orderDto, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/detail/{orderId}")
    public ResponseEntity getOrderDetail(@PathVariable("orderId") Long orderId,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Order order = orderService.retrieveById(orderId);

            // 해당 Address의 User ID와 현재 요청한 유저의 ID가 일치하는지 확인 (무결성 체크)
            if (!order.getUser().getId().equals(jwtService.getId(token))) {
                throw new UnauthorizedException("addressUnauthorizedAccess", "이 주소에 대한 접근 권한이 없습니다.");
            }

            OrderDto orderDto = orderService.getDto(order);

            return new ResponseEntity<>(orderDto, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

//    @GetMapping("/detail/{orderNumber}")
//    public ResponseEntity getOrderDetail(@PathVariable("orderNumber") String orderNumber,
//                                         @CookieValue(value = "token", required = false) String accessToken,
//                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
//                                         HttpServletResponse res) {
//
//        String token = jwtService.getToken(accessToken, refreshToken, res);
//
//        if (token != null) {
//            Long orderId = orderService.retrieveIdByOrderNumber(orderNumber);
//            List<OrderDetail> orderDetailList = orderDetailService.retrieveAllByOrderId(orderId);
//
//            List<OrderDetailDto> orderDetailDtoList = orderDetailList.stream().map(orderDetail -> {
//                Product product = orderDetail.getProduct();
//                Shipment shipment = orderDetail.getShipment();
//
//                OrderDetailDto orderDetailDto = orderDetailService.convertToDto(orderDetail);
//
//                orderDetailDto.includeProduct(productService.getProductDto(product));
//                orderDetailDto.includeShipment(shipmentService.convertToDto(shipment));
//
//                return orderDetailDto;
//            }).collect(Collectors.toList());
//
//            return new ResponseEntity<>(orderDetailDtoList, HttpStatus.OK);
//
//        } else { // 토큰이 유효하지 않은 경우
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//    }

    @PostMapping("/search")
    public ResponseEntity searchOrderList(@RequestBody OrderListSearchRequest orderListSearchRequest,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res,
                                          Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Order> orderPage = orderService.searchAllPaginated(orderListSearchRequest, pageable);
            List<OrderDto> orderDtoList = orderService.getDtoList(orderPage.getContent());

            OrderListResponse orderListResponse = OrderListResponse.builder()
                    .orders(orderDtoList)
                    .totalPages(orderPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(orderListResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @Transactional
    @PostMapping("/update/single")
    public ResponseEntity updateOrder(@RequestBody OrderDto orderDto,
                                      @CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);
        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        // 주문 DB 갱신
        Order order = orderService.retrieveById(orderDto.getId());
        orderService.updateOrder(order, orderDto);

        // 배송 DB 갱신
        List<OrderDetailDto> orderDetailDtoList = orderDto.getOrderDetails();
        for (OrderDetailDto orderDetailDto : orderDetailDtoList) {
            ShipmentDto shipmentDto = orderDetailDto.getShipment();
            Shipment shipment = shipmentService.retrieveById(shipmentDto.getId());

            shipmentService.updateShipment(shipment, shipmentDto);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/multiple")
    public ResponseEntity updateOrders(@RequestBody List<OrderDto> orderDtoList,
                                       @CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        orderService.updateOrders(orderDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/cancel/{orderId}")
    public ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId,
                                      @CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Order order = orderService.retrieveById(orderId);
            String message = orderService.cancelOrder(order);

            return new ResponseEntity<>(message, HttpStatus.OK);

        } else {// 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity confirmOrder(@PathVariable("orderId") Long orderId,
                                       @CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Order order = orderService.retrieveById(orderId);
            String message = orderService.confirmOrder(order);

            return new ResponseEntity<>(message, HttpStatus.OK);

        } else {// 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
