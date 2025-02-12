package org.fablewhirl.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<UserReadDto>> getAllUsers() {
        List<UserReadDto> users = userService.getAll();
        return users.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<UserReadDto> getUserById(@AuthenticationPrincipal Jwt jwt) {
        return userService.getUserById(jwt.getSubject())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping()
    public ResponseEntity<UserReadDto> updateUser(@AuthenticationPrincipal Jwt jwt,
                                                  @Valid @RequestBody UserCreateEditDto userEditDto) {
        return ResponseEntity.ok(userService.updateUser(jwt.getSubject(), userEditDto));
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();

        if (userService.existsById(userId)) {
            userService.deleteUser(userId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}




