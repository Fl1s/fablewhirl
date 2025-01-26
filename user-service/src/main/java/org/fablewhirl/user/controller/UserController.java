package org.fablewhirl.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.mapper.UserReadMapper;
import org.fablewhirl.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserReadMapper userReadMapper;

    @PostMapping("/register")
    public ResponseEntity<UserCreateEditDto> register(
            @Valid @RequestBody UserCreateEditDto userData) {

        return ResponseEntity.ok(userService.register(userData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserReadDto> getUserById(
            @PathVariable @NotBlank String id) {

        UserReadDto user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserReadDto>> getAllUsers() {
        List<UserReadDto> users = userService.getAll().stream()
                .map(userReadMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserCreateEditDto> updateUser(
            @PathVariable @NotBlank String id,
            @Valid @RequestBody UserCreateEditDto userRegistrationDto) {

        return ResponseEntity.ok(userService.updateUser(id, userRegistrationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable @NotBlank String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}


