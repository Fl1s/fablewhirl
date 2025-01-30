package org.fablewhirl.thread.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "thread-service",
                version = "v1",
                description = "Responsible for the logic of threads, their posting and linking comments."
        )
)
public class SwaggerConfig {
}
