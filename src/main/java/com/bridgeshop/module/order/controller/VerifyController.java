package com.bridgeshop.module.order.controller;

import com.bridgeshop.module.cart.entity.Cart;
import com.bridgeshop.module.cart.service.CartProductService;
import com.bridgeshop.module.cart.service.CartService;
import com.bridgeshop.module.order.entity.Order;
import com.bridgeshop.module.shipment.entity.Shipment;
import com.bridgeshop.module.order.service.OrderDetailService;
import com.bridgeshop.module.order.service.OrderService;
import com.bridgeshop.module.shipment.service.ShipmentService;
import com.bridgeshop.module.user.service.JwtService;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/verifyIamport")
public class VerifyController {

    // Iamport 결제 검증 컨트롤러
//    private final IamportClient iamportClient;
    private IamportClient iamportClient;
    private final CartService cartService;
    private final CartProductService cartProductService;
    private final OrderDetailService orderDetailService;
    private final ShipmentService shipmentService;

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
//    public VerifyController() {
//        this.iamportClient = new IamportClient(apiKey, apiSecret);
//    }

    /**
     * 프론트에서 받은 PG사 결괏값을 통해 아임포트 토큰 발행
     **/
    @PostMapping("/{imp_uid}")
    public IamportResponse<Payment> paymentByImpUid(@PathVariable String imp_uid,
                                                    @CookieValue(value = "token", required = false) String accessToken,
                                                    @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                    HttpServletResponse res) throws IamportResponseException, IOException {

        String token = jwtService.getToken(accessToken, refreshToken, res); // 추가
        Long userId = jwtService.getId(token);

        IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(imp_uid);
        Payment payment = iamportResponse.getResponse();

        // 주문, 배송, 주문 상세 정보 작성
        Order order = orderService.insertOrder(userId, payment);
        Shipment shipment = shipmentService.insertShipment(payment);
        orderDetailService.insertOrderDetail(userId, order, shipment);

        // 카트 상품 삭제
        Cart cart = cartService.retrieveByUserId(userId);
        cartProductService.deleteByCartId(cart.getId());

        return iamportResponse;
    }
}