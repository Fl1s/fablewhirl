package org.fablewhirl.apigateway.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

    @Bean
    public RouterFunction<ServerResponse> authServiceRoute() {
        return route("auth_service")
                .route(RequestPredicates.path("/api/auth/**"),
                        HandlerFunctions.http("http://localhost:8081"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("authServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return route("user_service")
                .route(RequestPredicates.path("/api/users/**"),
                        HandlerFunctions.http("http://localhost:8082"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("userServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> characterServiceRoute() {
        return route("character_service")
                .route(RequestPredicates.path("/api/characters/**"),
                        HandlerFunctions.http("http://localhost:8083"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("characterServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> threadServiceRoute() {
        return route("thread_service")
                .route(RequestPredicates.path("/api/threads/**"),
                        HandlerFunctions.http("http://localhost:8084"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("threadServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> notificationServiceRoute() {
        return route("notification_service")
                .route(RequestPredicates.path("/api/notifications/**"),
                        HandlerFunctions.http("http://localhost:8085"))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("notificationServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute",
                        request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                                .body("[ 'Service Unavailable yet... Please try again later.' ]"))
                .build();
    }
}
