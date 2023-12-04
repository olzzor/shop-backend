package com.bridgeshop.integration.feign.facebook;

import com.bridgeshop.config.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "facebookAuth", url = "https://graph.facebook.com", configuration = {FeignConfiguration.class})
public interface FacebookAuthApi {
    @PostMapping("/v12.0/oauth/access_token")
    ResponseEntity<String> getAccessToken(@RequestBody FacebookRequestAccessTokenDto requestDto);
}