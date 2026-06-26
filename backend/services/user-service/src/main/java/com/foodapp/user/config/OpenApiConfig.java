package com.foodapp.user.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI userServiceOpenApi() {
        return new OpenAPI().info(new Info()
                .title("User Service API")
                .description("User accounts (customers, restaurants, admins) for the food-ordering platform.")
                .version("v1"));
    }
}
