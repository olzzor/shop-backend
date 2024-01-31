package com.bridgeshop.module.user.service;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;

public interface JwtService {
    //    String getToken(String key, Object value);
    String createAccessToken(Long id);

    String createRefreshToken(Long id);

    String createPasswordResetToken(Long id);

    String reissueToken(String token);

    Claims getClaims(String token);

    boolean isValid(String token);

    String getToken(String accessToken, String refreshToken, HttpServletResponse res);

    Long getId(String token);
}
