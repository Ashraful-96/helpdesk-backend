package com.aust.its.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("HelpDesk").description("AUST Issue Tracking System").version("v1.0.0"))
                .addSecurityItem(new SecurityRequirement().addList("HelpDesk"))
                .components(new Components().addSecuritySchemes("HelpDesk", new SecurityScheme()
                        .name("HelpDesk").type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")));
    }
}
