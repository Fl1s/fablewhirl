package org.fablewhirl.user.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.service.UserMediaService;
import org.fablewhirl.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users/{userId}/media")
@RequiredArgsConstructor
public class MediaController {

    private final UserMediaService userMediaService;
    private final UserService userService;

    @PatchMapping("/uploadAvatar")
    public ResponseEntity<Void> uploadAvatar(
            @PathVariable @NotBlank String userId,
            @RequestParam("file") @NotNull MultipartFile file) {

        if (!file.getContentType().startsWith("image") || !userService.existsById(userId)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            userMediaService.uploadAvatar(userId, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/uploadBanner")
    public ResponseEntity<Void> uploadBanner(
            @PathVariable @NotBlank String userId,
            @RequestParam("file") @NotNull MultipartFile file) {

        if (!file.getContentType().startsWith("image") || !userService.existsById(userId)) {
            return ResponseEntity.badRequest().build();
        }

        try {
            userMediaService.uploadBanner(userId, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getAvatar")
    public ResponseEntity<String> getAvatar(@PathVariable @NotBlank String userId) {
        return ResponseEntity.ok(userMediaService.getAvatarUrl(userId));
    }

    @GetMapping("/getBanner")
    public ResponseEntity<String> getBanner(@PathVariable @NotBlank String userId) {
        return ResponseEntity.ok(userMediaService.getBannerUrl(userId));
    }
}


