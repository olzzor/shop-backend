package com.bridgeshop.module.user.controller;

import com.bridgeshop.common.service.SendMailService;
import com.bridgeshop.common.util.CookieUtils;
import com.bridgeshop.integration.feign.common.SocialLoginRequest;
import com.bridgeshop.module.cart.service.CartService;
import com.bridgeshop.module.payload.LoginRequest;
import com.bridgeshop.module.user.dto.*;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.module.user.service.JwtService;
import com.bridgeshop.module.user.service.PasswordResetTokenService;
import com.bridgeshop.module.user.service.RefreshTokenService;
import com.bridgeshop.module.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final JwtService jwtService;
    private final UserService userService;
    private final CartService cartService;
    private final SendMailService sendMailService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordResetTokenService passwordResetTokenService;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable("id") Long id) {
//        return ResponseEntity.ok(userService.getUser(id));
        return ResponseEntity.ok(userService.getUserResponse(id));
    }

    @GetMapping("/email")
    public ResponseEntity<?> getUserEmail(@CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        return ResponseEntity.ok(userService.getUserResponse(jwtService.getId(token)).getEmail());

    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequest loginRequest,
                                HttpServletResponse res) {

        LoginResponse loginResponse = userService.doLogin(loginRequest);

        Long id = loginResponse.getId();

        String aToken = jwtService.createAccessToken(id);
        String rToken = jwtService.createRefreshToken(id);

        refreshTokenService.deleteRefreshToken(id);
        refreshTokenService.addRefreshToken(rToken, userService.retrieveById(id));

        CookieUtils.addCookie(res, cookieDomain, "token", aToken);
        CookieUtils.addCookie(res, cookieDomain, "refresh_token", rToken);

        return new ResponseEntity<>(loginResponse, HttpStatus.OK);

    }

    @PostMapping("/social-login")
//    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request) {
//        return ResponseEntity.created(URI.create("/social-login")).body(userService.doSocialLogin(request));
//    }
    public ResponseEntity<LoginResponse> doSocialLogin(@RequestBody @Valid SocialLoginRequest request,
                                                       HttpServletResponse res) {

        LoginResponse loginResponse = userService.doSocialLogin(request);

        Long id = loginResponse.getId();

        String aToken = jwtService.createAccessToken(id);
        String rToken = jwtService.createRefreshToken(id);

        refreshTokenService.deleteRefreshToken(id);
        refreshTokenService.addRefreshToken(rToken, userService.retrieveById(id));

        CookieUtils.addCookie(res, cookieDomain, "token", aToken);
        CookieUtils.addCookie(res, cookieDomain, "refresh_token", rToken);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@CookieValue(value = "token", required = false) String accessToken,
                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                 HttpServletRequest req, HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {

            refreshTokenService.deleteRefreshToken(jwtService.getId(token));

            CookieUtils.deleteCookie(req, res, cookieDomain, "token");
            CookieUtils.deleteCookie(req, res, cookieDomain, "refresh_token");

            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/check")
    public ResponseEntity check(@CookieValue(value = "token", required = false) String accessToken,
                                @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Long userId = jwtService.getId(token);

            LoginResponse loginResponse = LoginResponse.builder()
                    .id(userId)
                    .role(userService.isAdminRole(userId) ? "admin" : "user")
                    .build();

            return new ResponseEntity<>(loginResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }

    /**
     * 추가
     */
    @GetMapping("/check-admin-role")
    public ResponseEntity<?> checkAdminRole(@CookieValue(value = "token", required = false) String accessToken,
                                            @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                            HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            return new ResponseEntity<>(userService.isAdminRole(jwtService.getId(token)), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
    }

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody UserJoinRequest userJoinRequest) {

        Long userId = userService.createAccount(userJoinRequest);
        cartService.createCart(userId);

        return new ResponseEntity<>(userId, HttpStatus.OK);
    }

    /**
     * 회원 정보 변경
     */
    @PostMapping(value = "/change-profile")
    public ResponseEntity changeProfile(@RequestBody UserUpdateRequest userUpdateRequest,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Long userId = jwtService.getId(token);
            userService.updateProfile(userId, userUpdateRequest);

            return new ResponseEntity<>(userId, HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 비밀번호 변경
     */
    @PostMapping(value = "/change-password")
    public ResponseEntity changePassword(@RequestBody UserUpdateRequest userUpdateRequest,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Long userId = jwtService.getId(token);
            userService.updatePassword(userId, userUpdateRequest);

            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 비밀번호 재설정 토큰 검증
     */
    @GetMapping(value = "/verify-password-reset-token/{token}")
    public ResponseEntity verifyPasswordResetToken(@PathVariable String token) {
        boolean isValid = passwordResetTokenService.validatePasswordResetToken(token);

        if (isValid) {
            // 입력된 비밀번호 재설정 토큰이 유효한 경우
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 입력된 비밀번호 재설정 토큰이 유효하지않은 경우
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 비밀번호 찾기 (재설정 링크 접근 토큰 발행 및 메일 전송)
     */
    @PostMapping(value = "/find-password")
    public ResponseEntity findPassword(@RequestBody UserDto userDto) {

        // 입력된 이메일의 계정 정보를 취득
        User user = userService.getActiveUserByEmail(userDto.getEmail());

        // 비밀번호 재설정 토큰 생성 및 저장
        String prToken = jwtService.createPasswordResetToken(user.getId());
        passwordResetTokenService.addPasswordResetToken(prToken, user);

        // 비밀번호 재설정 이메일 전송
        sendMailService.sendPasswordResetMail(user.getEmail(), prToken);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 비밀번호 재설정
     */
    @PostMapping(value = "/reset-password")
    public ResponseEntity resetPassword(@RequestBody PasswordResetRequest prReq) {
        boolean isValid = passwordResetTokenService.validatePasswordResetToken(prReq.getToken());

        if (isValid) {
            // 입력된 비밀번호 재설정 토큰이 유효한 경우
            userService.updatePassword(prReq);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            // 입력된 비밀번호 재설정 토큰이 유효하지않은 경우
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

//    /**
//     * 비밀번호 찾기 (임시 비밀번호 발급 및 메일 전송)
//     */
//    @PostMapping(value = "/recover-account")
//    public ResponseEntity recoverAccount(@RequestBody UserDto userDto) {
//
//        // 입력된 이메일의 계정 정보를 취득
//        User user = userService.getUserByEmail(userDto.getEmail());
//
//        MailDto maildto = sendMailService.writeTempPasswordMail(user.getEmail(), user.getName());
//        sendMailService.sendHtmlMail(maildto);
//
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PostMapping(value = "/withdraw")
    public ResponseEntity withdrawAccount(@CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletRequest req, HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Long userId = jwtService.getId(token);

            userService.deactivateUser(userId);
            cartService.deactivateCart(userId);

            CookieUtils.deleteCookie(req, res, cookieDomain, "token");

            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/get")
    public ResponseEntity getUser(@CookieValue(value = "token", required = false) String accessToken,
                                  @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                  HttpServletResponse res,
                                  Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            UserResponse userResponse = userService.getUserResponse(jwtService.getId(token));
            return new ResponseEntity<>(userResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/list")
    public ResponseEntity getUserList(@CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res,
                                      Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {

            Page<User> userPage = userService.retrieveAllPaginated(pageable);
            List<UserDto> userDtoList = userService.convertToDtoList(userPage.getContent());

            UserListResponse userListResponse = UserListResponse.builder()
                    .users(userDtoList)
                    .totalPages(userPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(userListResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/search")
    public ResponseEntity searchUserList(@RequestBody UserListSearchRequest userListSearchRequest,
                                         @CookieValue(value = "token", required = false) String accessToken,
                                         @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                         HttpServletResponse res,
                                         Pageable pageable) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Page<User> userPage = userService.searchUserList(userListSearchRequest, pageable);
            List<UserDto> userDtoList = userService.convertToDtoList(userPage.getContent());

            UserListResponse userListResponse = UserListResponse.builder()
                    .users(userDtoList)
                    .totalPages(userPage.getTotalPages())
                    .build();

            return new ResponseEntity<>(userListResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/update/multiple")
    public ResponseEntity updateUsers(@RequestBody List<UserDto> userDtoList,
                                      @CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            userService.updateUsers(userDtoList);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
