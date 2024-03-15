package com.shop.module.order.controller;

import com.shop.common.service.SendMailService;
import com.shop.module.cart.entity.Cart;
import com.shop.module.cart.service.CartProductService;
import com.shop.module.cart.service.CartService;
import com.shop.module.order.dto.OrderPaymentRequest;
import com.shop.module.order.entity.Order;
import com.shop.module.order.entity.OrderDetail;
import com.shop.module.order.service.OrderDetailService;
import com.shop.module.order.service.OrderService;
import com.shop.module.shipment.entity.Shipment;
import com.shop.module.shipment.service.ShipmentService;
import com.shop.module.user.service.JwtService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

    private IamportClient iamportClient;

    private final CartService cartService;
    private final CartProductService cartProductService;
    private final OrderDetailService orderDetailService;
    private final ShipmentService shipmentService;
    private final SendMailService sendMailService;

    @Autowired
    JwtService jwtService;
    @Autowired
    OrderService orderService;

    @Value("${iamport.apiKey}")
    private String apiKey;
    @Value("${iamport.apiSecret}")
    private String apiSecret;

    @PostConstruct
    public void init() {
        this.iamportClient = new IamportClient(apiKey, apiSecret);
    }

    @PostMapping("/payment-gateway/{imp_uid}")
    public IamportResponse<Payment> payOnDesktop(@PathVariable String imp_uid,
                                                 @CookieValue(value = "token", required = false) String accessToken,
                                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                 HttpServletResponse res) throws IamportResponseException, IOException {

        String token = jwtService.getToken(accessToken, refreshToken, res);
        Long userId = jwtService.getId(token);

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid);
        Payment payment = iamportResponse.getResponse();

        // 주문, 배송, 주문 상세 정보 작성
        Order order = orderService.insertOrder(userId, payment);
        Shipment shipment = shipmentService.insertShipment(payment);
        List<OrderDetail> orderDetailList = orderDetailService.insertOrderDetail(userId, order, shipment);

        // 카트 상품 삭제
        Cart cart = cartService.retrieveByUserId(userId);
        cartProductService.deleteByCartId(cart.getId());

        // 주문 정보 안내 메일 전송
        sendMailService.sendOrderMail(order, shipment, orderDetailList);

        return iamportResponse;
    }

    @PostMapping("/payment-gateway/mobile/{imp_uid}")
    public ResponseEntity<?> payOnMobile(@PathVariable String imp_uid,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res) throws IamportResponseException, IOException {

        String token = jwtService.getToken(accessToken, refreshToken, res);
        Long userId = jwtService.getId(token);

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid);
        Payment payment = iamportResponse.getResponse();

        // 주문, 배송, 주문 상세 정보 작성
        Order order = orderService.insertOrder(userId, payment);
        Shipment shipment = shipmentService.insertShipment(payment);
        List<OrderDetail> orderDetailList = orderDetailService.insertOrderDetail(userId, order, shipment);

        // 카트 상품 삭제
        Cart cart = cartService.retrieveByUserId(userId);
        cartProductService.deleteByCartId(cart.getId());

        // 주문 정보 안내 메일 전송
        sendMailService.sendOrderMail(order, shipment, orderDetailList);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/direct-deposit")
    public ResponseEntity<?> directDeposit(@RequestBody OrderPaymentRequest orderPaymentRequest,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) throws IamportResponseException, IOException {

        String token = jwtService.getToken(accessToken, refreshToken, res);
        Long userId = jwtService.getId(token);

        // 주문, 배송, 주문 상세 정보 작성
        Order order = orderService.insertOrderForDirectDeposit(userId, orderPaymentRequest);
        Shipment shipment = shipmentService.insertShipmentForDirectDeposit(orderPaymentRequest);
        List<OrderDetail> orderDetailList = orderDetailService.insertOrderDetail(userId, order, shipment);

        // 카트 상품 삭제
        Cart cart = cartService.retrieveByUserId(userId);
        cartProductService.deleteByCartId(cart.getId());

        // 주문 정보 안내 메일 전송
        sendMailService.sendOrderMail(order, shipment, orderDetailList);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}