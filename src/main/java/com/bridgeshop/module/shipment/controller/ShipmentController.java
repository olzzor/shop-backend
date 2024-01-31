package com.bridgeshop.module.shipment.controller;

import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.module.shipment.dto.ShipmentDto;
import com.bridgeshop.module.shipment.dto.ShipmentListResponse;
import com.bridgeshop.module.shipment.dto.ShipmentListSearchRequest;
import com.bridgeshop.module.shipment.entity.Shipment;
import com.bridgeshop.module.shipment.service.ShipmentService;
import com.bridgeshop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
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
@RequestMapping("/api/shipment")
public class ShipmentController {

    private final JwtService jwtService;
    private final ShipmentService shipmentService;

    @GetMapping("/list")
    public ResponseEntity getShipmentList(@CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res,
                                          Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Shipment> shipmentPage = shipmentService.getShipmentList(pageable);
            List<ShipmentDto> shipmentDtoList = shipmentService.getShipmentDtoList(shipmentPage.getContent());

            ShipmentListResponse shipmentListResponse = ShipmentListResponse.builder()
                    .shipments(shipmentDtoList)
                    .totalPages(shipmentPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(shipmentListResponse, HttpStatus.OK);

        } else { // 토큰이 유효하지 않은 경우
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/search")
    public ResponseEntity searchShipmentList(@RequestBody ShipmentListSearchRequest shipmentListSearchRequest,
                                             @CookieValue(value = "token", required = false) String accessToken,
                                             @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                             HttpServletResponse res,
                                             Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<Shipment> shipmentPage = shipmentService.searchShipmentList(shipmentListSearchRequest, pageable);
            List<ShipmentDto> shipmentDtoList = shipmentService.getShipmentDtoList(shipmentPage.getContent());

            ShipmentListResponse shipmentListResponse = ShipmentListResponse.builder()
                    .shipments(shipmentDtoList)
                    .totalPages(shipmentPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(shipmentListResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/update/multiple")
    public ResponseEntity updateShipments(@RequestBody List<ShipmentDto> shipmentDtoList,
                                          @CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        shipmentService.updateShipments(shipmentDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
