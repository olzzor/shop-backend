package com.bridgeshop.module.user.service;

import com.bridgeshop.module.user.entity.RefreshToken;
import com.bridgeshop.module.user.repository.RefreshTokenRepository;
import com.bridgeshop.common.util.CookieUtils;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service("jwtService")
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final String secretKey = "abbci2ioadij@@@ai17a662###8139!!!";

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @Override
    public String createAccessToken(Long userId) {
        Date expTime = new Date();
        expTime.setTime(expTime.getTime() + 1000 * 60 * 30); // 30분 유효기간
        return createToken("id", userId, expTime);
    }

    @Override
    public String createRefreshToken(Long userId) {
        Date expTime = new Date();
        expTime.setTime(expTime.getTime() + 1000 * 60 * 60 * 24 * 7); // 7일 유효기간
        return createToken("id", userId, expTime);
    }

    private String createToken(String key, Object value, Date expTime) {

        byte[] secretByteKey = Base64.decode(Base64.encode(secretKey.getBytes()));
        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("typ", "JWT");
        headerMap.put("alg", "HS256");

        Map<String, Object> map = new HashMap<>();
        map.put(key, value);

        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
                .setClaims(map)
                .setExpiration(expTime)
                .signWith(signKey, SignatureAlgorithm.HS256);

        return builder.compact();
    }

    @Override
    public String reissueToken(String token) {
        Long userId = getId(token);

        // 사용자의 리프레시 토큰 취득
        RefreshToken rt = refreshTokenRepository.findByUser_Id(userId);

        // 리프레시 토큰의 유효성 검사
        if (rt != null
                && rt.getToken().equals(token)
                && rt.getExpDate().isAfter(LocalDateTime.now())) {

            // 새 액세스 토큰 발급
            return createAccessToken(userId);
        } else {
            // 리프레시 토큰이 유효하지 않거나 만료된 경우
            throw new IllegalArgumentException("Invalid refresh token");
        }
    }

    @Override
    public Claims getClaims(String token) {
        if (token != null && !"".equals(token)) {
            try {
                byte[] secretByteKey = Base64.decode(Base64.encode(secretKey.getBytes()));
                Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
                return Jwts.parserBuilder().setSigningKey(signKey).build().parseClaimsJws(token).getBody();
            } catch (ExpiredJwtException e) {
                // 만료된 토큰에 대한 처리
            } catch (JwtException e) {
                // 유효하지 않은 토큰에 대한 처리
            }
        }

        return null;
    }

    @Override
    public String getToken(String accessToken, String refreshToken, HttpServletResponse res) {
        if (getClaims(accessToken) != null) {
            // 액세시 토큰 유효시, 해당 토큰 반환
            return accessToken;

        } else if (getClaims(refreshToken) != null) {
            // 액세스 토큰 만료시, 토큰 재발급 및 반환
            String token = reissueToken(refreshToken);
            CookieUtils.addCookie(res, cookieDomain, "token", token);

            return token;
        } else {
            return null;
        }
    }

    @Override
    public Long getId(String token) {
        Claims claims = getClaims(token);

        if (claims != null) {
            return Long.parseLong(claims.get("id").toString());
        }

        return null;
    }

    //    @Override
//    public String getToken(String key, Object value) {
//
//        Date expTime = new Date();
//        expTime.setTime(expTime.getTime() + 1000 * 60 * 30);
//        byte[] secretByteKey = Base64.decode(Base64.encode(secretKey.getBytes()));
//        Key signKey = new SecretKeySpec(secretByteKey, SignatureAlgorithm.HS256.getJcaName());
//
//        Map<String, Object> headerMap = new HashMap<>();
//        headerMap.put("typ", "JWT");
//        headerMap.put("alg", "HS256");
//
//        Map<String, Object> map = new HashMap<>();
//        map.put(key, value);
//
//        JwtBuilder builder = Jwts.builder().setHeader(headerMap)
//                .setClaims(map)
//                .setExpiration(expTime)
//                .signWith(signKey, SignatureAlgorithm.HS256);
//
//        return builder.compact();
//    }
    @Override
    public boolean isValid(String token) {
        return getClaims(token) != null;
    }
}
