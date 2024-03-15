package com.shop.module.contact.controller;

import com.shop.module.contact.dto.ContactDto;
import com.shop.module.contact.dto.ContactListResponse;
import com.shop.module.contact.dto.ContactListSearchRequest;
import com.shop.module.contact.entity.Contact;
import com.shop.module.contact.service.ContactService;
import com.shop.common.exception.UnauthorizedException;
import com.shop.module.user.service.JwtService;
import com.shop.common.service.SendMailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contact")
public class ContactController {

    private final JwtService jwtService;
    private final ContactService contactService;
    private final SendMailService sendMailService;

    @GetMapping("/history")
    public ResponseEntity getContactHistory(@CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        List<Contact> contactList = contactService.retrieveAllByUserId(jwtService.getId(token));
        List<ContactDto> contactDtoList = contactService.convertToDtoList(contactList);
        return new ResponseEntity<>(contactDtoList, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity getContactList(@CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res,
                                         Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        ContactListResponse contactListResponse = contactService.getContactList(pageable);
        return new ResponseEntity<>(contactListResponse, HttpStatus.OK);
    }

    @GetMapping("/detail/{contactId}")
    public ResponseEntity getContactDetail(@PathVariable("contactId") Long contactId,
                                           @CookieValue(value = "token", required = false) String accessToken,
                                           @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                           HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        List<Contact> contactList = contactService.getContactDetail(jwtService.getId(token), contactId);
        List<ContactDto> contactDtoList = contactService.convertToDtoList(contactList);

        return new ResponseEntity<>(contactDtoList, HttpStatus.OK);
    }

    @PostMapping("/search")
    public ResponseEntity searchContactList(@RequestBody ContactListSearchRequest contactListSearchRequest,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res,
                                            Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        ContactListResponse contactListResponse = contactService.searchContactList(contactListSearchRequest, pageable);
        return new ResponseEntity<>(contactListResponse, HttpStatus.OK);
    }

    @PostMapping("/inquiry")
    public ResponseEntity pushContactInquiry(@RequestBody ContactDto contactDto,
                                             @CookieValue(value = "token", required = false) String accessToken,
                                             @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                             HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            contactService.createInquiry(null, contactDto);
        } else {
            contactService.createInquiry(jwtService.getId(token), contactDto);
        }

        sendMailService.sendContactInquiryMail(contactDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/answer")
    public ResponseEntity pushContactAnswer(@RequestBody ContactDto contactDto,
                                            @CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) { // 토큰이 유효하지 않은 경우
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        contactService.createAnswer(jwtService.getId(token), contactDto);
        sendMailService.sendContactAnswerMail(contactDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/update/multiple")
    public ResponseEntity updateContacts(@RequestBody List<ContactDto> contactDtoList,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new UnauthorizedException("tokenInvalid", "유효하지 않은 토큰입니다.");
        }

        contactService.updateContacts(contactDtoList);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
