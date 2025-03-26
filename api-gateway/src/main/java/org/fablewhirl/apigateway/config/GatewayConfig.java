package org.fablewhirl.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class GatewayConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth_service_docs", r -> r.path("/aggregate/auth-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/auth-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://auth-service:8081"))

                .route("user_service_docs", r -> r.path("/aggregate/user-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/user-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://user-service:8082"))

                .route("character_service_docs", r -> r.path("/aggregate/character-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/character-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://character-service:8083"))

                .route("thread_service_docs", r -> r.path("/aggregate/thread-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/thread-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://thread-service:8084"))

                .route("comment_service_docs", r -> r.path("/aggregate/comment-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/comment-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://comment-service:8085"))

                .route("notification_service_docs", r -> r.path("/aggregate/notification-service/v3/api-docs")
                        .filters(f -> f.rewritePath("/aggregate/notification-service/v3/api-docs", "/v3/api-docs"))
                        .uri("http://notification-service:8086"))

                // Main Service Routes
                .route("auth_service", r -> r.path("/api/v1/auth/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("authServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://auth-service:8081"))

                .route("user_service", r -> r.path("/api/v1/users/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("userServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://user-service:8082"))

                .route("character_service", r -> r.path("/api/v1/characters/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("characterServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://character-service:8083"))

                .route("thread_service", r -> r.path("/api/v1/threads/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("threadServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://thread-service:8084"))

                .route("comment_service", r -> r.path("/api/v1/comments/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("commentServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://comment-service:8085"))

                .route("notification_service", r -> r.path("/api/v1/notifications/**")
                        .filters(f -> f.circuitBreaker(c -> c.setName("notificationServiceCircuitBreaker")
                                .setFallbackUri("forward:/fallbackRoute")))
                        .uri("http://notification-service:8086"))

                .route("fallbackRoute", r -> r.path("/fallbackRoute")
                        .filters(f -> f.rewritePath("/fallbackRoute", "/"))
                        .uri("forward:/fallback"))

                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackHandler() {
        return RouterFunctions.route()
                .GET("/fallback", request -> ServerResponse
                        .status(org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE)
                        .bodyValue("[ 'Service Unavailable. Please try again later.' ]"))
                .build();
    }
}
