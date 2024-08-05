package com.bangguddle.ownbang.global.config.openvidu;

import io.openvidu.java.client.OpenVidu;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenviduConfig {

    @Value("${openvidu.secret}")
    private String secret;

    @Value("${openvidu.url}")
    private String openviduUrl;

    @Bean
    public OpenVidu openVidu() {
        return new OpenVidu(openviduUrl, secret);
    }
}