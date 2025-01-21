package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserMediaDto;
import org.fablewhirl.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody UserCreateEditDto userData) {
        userService.register(userData);
    }

    @GetMapping("/{id}")
    public UserCreateEditDto getUserById(@PathVariable String id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserCreateEditDto updateUser(@PathVariable String id, @RequestBody UserCreateEditDto userRegistrationDto) {
        return userService.updateUser(id, userRegistrationDto);
    }
}
