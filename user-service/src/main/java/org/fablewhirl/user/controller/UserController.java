package org.fablewhirl.user.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.mapper.UserMapper;
import org.fablewhirl.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    public ResponseEntity<UserCreateEditDto> register(
            @Valid @RequestBody UserCreateEditDto userData) {

        return ResponseEntity.ok(userService.register(userData));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserCreateEditDto> getUserById(
            @PathVariable @NotBlank String id) {

        UserCreateEditDto user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserCreateEditDto>> getAllUsers() {
        List<UserCreateEditDto> users = userService.getAll().stream()
                .map(userMapper::toDto)
                .toList();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
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


