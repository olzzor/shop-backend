package com.shop.module.stats.controller;

import com.shop.module.stats.dto.StatsSalesCategoryRequest;
import com.shop.module.stats.dto.StatsSalesCategoryResponse;
import com.shop.module.stats.dto.StatsSalesRequest;
import com.shop.module.stats.dto.StatsSalesResponse;
import com.shop.module.stats.service.StatsService;
import com.shop.module.user.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/stats")
public class StatsController {

    private final JwtService jwtService;
    private final StatsService statsService;

    @PostMapping("/sales")
    public ResponseEntity getStatsSales(@RequestBody StatsSalesRequest statsSalesRequest,
                                        @CookieValue(value = "token", required = false) String accessToken,
                                        @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                        HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            List<StatsSalesResponse> lstStatsSalesResponse = new ArrayList<>();

            switch (statsSalesRequest.getType()) {
                case "year":
                    lstStatsSalesResponse = statsService.getYearlySales(statsSalesRequest);
                    break;
                case "month":
                    lstStatsSalesResponse = statsService.getMonthlySales(statsSalesRequest);
                    break;
                case "week":
                    lstStatsSalesResponse = statsService.getWeeklySales(statsSalesRequest);
                    break;
                default:
                    lstStatsSalesResponse = statsService.getDailySales(statsSalesRequest);
            }
            return new ResponseEntity<>(lstStatsSalesResponse, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/sales-category")
    public ResponseEntity getStatsSalesCategory(@RequestBody StatsSalesCategoryRequest statsSalesCategoryRequest,
                                                @CookieValue(value = "token", required = false) String accessToken,
                                                @CookieValue(value = "refresh_token", required = false) String refreshToken,
                                                HttpServletResponse res) {

        String token = jwtService.getToken(accessToken, refreshToken, res);

        if (token != null) {
            List<StatsSalesCategoryResponse> statsResList = statsService.getSalesCategory(statsSalesCategoryRequest);
            return new ResponseEntity<>(statsResList, HttpStatus.OK);

        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
    }
}
