package com.bangguddle.ownbang.global.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "온방 API 명세서",
                description = "<h3>OwnBang API Reference for Developers</h3>",
                version = "v0"
        )
)
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder().group("all").pathsToMatch("/**").build();
    }

}