package com.mspp.restaurantservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Restaurant Service API",
                version = "v1",
                description = "REST API for managing restaurants"
        )
)
public class OpenApiConfig {
}