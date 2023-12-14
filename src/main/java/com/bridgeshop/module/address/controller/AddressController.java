package com.bridgeshop.module.address.controller;

import com.bridgeshop.module.address.entity.Address;
import com.bridgeshop.module.address.dto.AddressDto;
import com.bridgeshop.module.address.service.AddressService;
import com.bridgeshop.module.address.dto.AddressUpdateRequest;
import com.bridgeshop.common.exception.UnauthorizedException;
import com.bridgeshop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/address")
public class AddressController {

    private final JwtService jwtService;
    private final AddressService addressService;

    @GetMapping("/default")
    public ResponseEntity getDefaultAddress(@CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Optional<Address> addressOptional = addressService.getDefaultAddressByUserId(jwtService.getId(token));
        AddressDto addressDto = addressOptional.map(addressService::convertToDto).orElse(AddressDto.builder().build());

        return new ResponseEntity<>(addressDto, HttpStatus.OK);
    }

    @GetMapping("/get/{addressId}")
    public ResponseEntity getAddress(@PathVariable("addressId") Long addressId,
                                     @CookieValue(value = "token", required = false) String accessToken,
                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Address address = addressService.retrieveById(addressId);
        AddressDto addressDto = addressService.convertToDto(address);

        return new ResponseEntity<>(addressDto, HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity getAddresses(@CookieValue(value = "token", required = false) String accessToken,
                                       @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                       HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        Long userId = jwtService.getId(token);
        List<Address> addressList = addressService.retrieveAllByUserId(userId);
        List<AddressDto> addressDtoList = addressService.convertToDtoList(addressList);

        return new ResponseEntity<>(addressDtoList, HttpStatus.OK);
    }

    @PostMapping(value = "/add")
    public ResponseEntity addAddress(@RequestBody AddressUpdateRequest addressUpdateRequest,
                                     @CookieValue(value = "token", required = false) String accessToken,
                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        addressService.checkInput(addressUpdateRequest);
        Long userId = jwtService.getId(token);

        // 추가 주소지를 기본 배송지로 설정한 경우, 기존 주소지의 기본 배송지 설정을 해제
        if (addressUpdateRequest.isDefault()) {
            addressService.updateDefaultAddress(userId);
        }
        addressService.insertAddress(userId, addressUpdateRequest);

        return new ResponseEntity<>(HttpStatus.OK);

    }

    @PostMapping(value = "/edit")
    public ResponseEntity editAddress(@RequestBody AddressUpdateRequest addressUpdateRequest,
                                      @CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        addressService.checkInput(addressUpdateRequest);
        Long userId = jwtService.getId(token);

        // 추가 주소지를 기본 배송지로 설정한 경우, 기존 주소지의 기본 배송지 설정을 해제
        if (addressUpdateRequest.isDefault()) {
            addressService.updateDefaultAddress(userId);
        }
        addressService.updateAddress(userId, addressUpdateRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/delete/{addressId}")
    public ResponseEntity deleteAddress(@PathVariable("addressId") Long addressId,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        addressService.deleteAddress(jwtService.getId(token), addressId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
