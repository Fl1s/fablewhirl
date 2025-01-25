package org.fablewhirl.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service", r -> r.path("/auth/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("authServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://localhost:8081"))

                .route("user_service", r -> r.path("/users/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("userServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://localhost:8082"))

                .route("character_service", r -> r.path("/characters/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("characterServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://localhost:8083"))

                .route("thread_service", r -> r.path("/threads/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("threadServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://localhost:8084"))

                .route("comment_service", r -> r.path("/comments/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("commentServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://localhost:8085"))

                .route("notification_service", r -> r.path("/notifications/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("notificationServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://localhost:8086"))

                .route("fallbackRoute", r -> r.path("/fallbackRoute")
                        .filters(f -> f.rewritePath("/fallbackRoute", "/"))
                        .uri("forward:/fallback"))

                .build();
    }

    @Bean
    public org.springframework.web.reactive.function.server.RouterFunction<org.springframework.web.reactive.function.server.ServerResponse> fallbackHandler() {
        return org.springframework.web.reactive.function.server.RouterFunctions.route()
                .GET("/fallback", request -> org.springframework.web.reactive.function.server.ServerResponse
                        .status(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)
                        .bodyValue("[ 'Service Unavailable. Please try again later.' ]"))
                .build();
    }
}
