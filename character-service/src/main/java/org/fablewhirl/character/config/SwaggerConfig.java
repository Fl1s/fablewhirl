package org.fablewhirl.character.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "character-service",
                description = "Responsible for character logic, their crud operations and linking to user by userId.",
                version = "v1"
        )
)
public class SwaggerConfig {
}

