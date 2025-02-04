package org.fablewhirl.comment.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "comment-service",
                version = "v1",
                description = "Responsible for the logic of comments, their posting and linking to posts."
        )
)
public class SwaggerConfig {
}

