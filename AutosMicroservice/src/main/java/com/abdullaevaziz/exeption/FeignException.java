package com.abdullaevaziz.exeption;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignException {
    @Bean
    public ErrorDecoder userClientErrorDecoder() {
        return new CustomFeignErrorDecoder();
    }
}
