package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserDto;
import org.fablewhirl.user.dto.UserMediaDto;
import org.fablewhirl.user.dto.UserRegistrationDto;
import org.fablewhirl.user.service.UserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserRegistrationDto userRegistrationDto) {
        return userService.updateUser(id, userRegistrationDto);
    }

    @PutMapping("/{userId}/media")
    public UserMediaDto updateUserMedia(@PathVariable String userId, @RequestBody UserMediaDto userMediaDto) {
        return userService.updateUserMedia(userId, userMediaDto);
    }
}
