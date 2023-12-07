package com.bridgeshop.module.user.controller;

import com.bridgeshop.module.cart.service.CartService;
import com.bridgeshop.module.user.dto.LoginResponse;
import com.bridgeshop.common.dto.MailDto;
import com.bridgeshop.integration.feign.common.SocialLoginRequest;
import com.bridgeshop.module.payload.LoginRequest;
import com.bridgeshop.module.user.dto.*;
import com.bridgeshop.module.user.service.UserService;
import com.bridgeshop.module.user.service.JwtService;
import com.bridgeshop.module.user.service.RefreshTokenService;
import com.bridgeshop.module.user.service.SendMailService;
import com.bridgeshop.module.user.entity.User;
import com.bridgeshop.common.util.CookieUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.UnsupportedEncodingException;
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

        CookieUtils.addCookie(res, "token", aToken);
        CookieUtils.addCookie(res, "refresh_token", rToken);

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

        CookieUtils.addCookie(res, "token", aToken);
        CookieUtils.addCookie(res, "refresh_token", rToken);

        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity logout(@CookieValue(value = "token", required = false) String accessToken,
                                 @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                 HttpServletRequest req, HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {

            refreshTokenService.deleteRefreshToken(jwtService.getId(token));

            CookieUtils.deleteCookie(req, res, "token");
            CookieUtils.deleteCookie(req, res, "refresh_token");

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

    @PostMapping(value = "/recover")
    public ResponseEntity recoverAccount(@RequestBody UserDto userDto) {

        String email = userDto.getEmail();

        if (userService.existUserByEmail(email)) {
            // 입력된 이메일의 계정이 존재하는 경우
            try {
                User user = userService.getUserByEmail(email);

                MailDto maildto = sendMailService.writeTempPasswordMail(email, user.getName());
                sendMailService.sendMail(maildto);

                return new ResponseEntity<>("Success", HttpStatus.OK);
            } catch (MessagingException | UnsupportedEncodingException ex) {
                return new ResponseEntity<>("EmailSendFailure", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            // 입력된 이메일의 계정이 존재하지 않는 경우
            return new ResponseEntity<>("Failure", HttpStatus.OK);
        }
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

    @PostMapping(value = "/withdraw")
    public ResponseEntity withdrawAccount(@CookieValue(value = "token", required = false) String accessToken,
                                          @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                          HttpServletRequest req, HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            Long userId = jwtService.getId(token);

            userService.deleteUser(userId);
            cartService.deactivateCart(userId);

            CookieUtils.deleteCookie(req, res, "token");

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

    @PostMapping(value = "/delete/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") Long id,
                                     @CookieValue(value = "token", required = false) String accessToken,
                                     @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                     HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            userService.deleteUser(id);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/update")
    public ResponseEntity updateUsers(@RequestBody List<UserDto> userDtos,
                                      @CookieValue(value = "token", required = false) String accessToken,
                                      @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                      HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            userService.updateUsers(userDtos);
            return new ResponseEntity<>(HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
