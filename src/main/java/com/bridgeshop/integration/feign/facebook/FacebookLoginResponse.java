package com.bridgeshop.integration.feign.facebook;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacebookLoginResponse {

    private String id;
    private String name;
    private String email;
}