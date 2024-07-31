package com.phildev.front.mls.config;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignSecurityConfig {

    @Value("${gateway-user}")
    private String gatewayUser;

    @Value("${gateway-pwd}")
    private String gatewayPassword;

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor(){
        return new BasicAuthRequestInterceptor(gatewayUser, gatewayPassword);
    }
}
