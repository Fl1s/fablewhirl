package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserMediaDto;
import org.fablewhirl.user.service.UserMediaService;
import org.fablewhirl.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class MediaController {
    private final UserMediaService userMediaService;

    @GetMapping("/media")
    public UserMediaDto updateUserMedia(@PathVariable String userId) {
        return userMediaService.getUserMedia(userId);
    }
    @PutMapping("/uploadAvatar")
    @ResponseStatus(HttpStatus.OK)
    public void uploadAvatar(@PathVariable String userId, @RequestParam("file") MultipartFile file) throws IOException {
        userMediaService.uploadAvatar(userId,file);
    }

    @PutMapping("/uploadBanner")
    @ResponseStatus(HttpStatus.OK)
    public void uploadBanner(@PathVariable String userId, @RequestParam("file") MultipartFile file) throws IOException {
        userMediaService.uploadBanner(userId,file);
    }
}
