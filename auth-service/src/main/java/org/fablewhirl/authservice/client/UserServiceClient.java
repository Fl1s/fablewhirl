package org.fablewhirl.authservice.client;

import org.fablewhirl.authservice.dto.RegisterDto;
import org.fablewhirl.authservice.dto.UserDto;
import org.fablewhirl.authservice.request.RegisterRequest;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/v1/user")
public interface UserServiceClient {
    @PostMapping("/register")
    ResponseEntity<RegisterDto> save(@RequestBody RegisterRequest request);

    @GetMapping("/getByUsername/{username}")
    ResponseEntity<UserDto> getUserByUsername(@PathVariable String username);
}