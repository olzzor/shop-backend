package com.shop.integration.feign.facebook;

import com.shop.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "facebookUser", url = "https://graph.facebook.com/v12.0", configuration = {FeignConfiguration.class})
public interface FacebookUserApi {
    @GetMapping("/me")
//    ResponseEntity<String> getUserInfo(@RequestParam("access_token") String accessToken);
    ResponseEntity<String> getUserInfo(@RequestParam("access_token") String accessToken, @RequestParam("fields") String fields);
}
