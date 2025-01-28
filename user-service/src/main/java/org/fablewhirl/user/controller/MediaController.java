package org.fablewhirl.user.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.service.UserMediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/users/{userId}/media")
@RequiredArgsConstructor
public class MediaController {

    private final UserMediaService userMediaService;

    @PatchMapping("/uploadAvatar")
    public ResponseEntity<Void> uploadAvatar(
            @PathVariable @NotBlank String userId,
            @RequestParam("file") @NotNull MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("File must be an image");
        }

        userMediaService.uploadAvatar(userId, file);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/uploadBanner")
    public ResponseEntity<Void> uploadBanner(
            @PathVariable @NotBlank String userId,
            @RequestParam("file") @NotNull MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("File must be an image");
        }

        userMediaService.uploadBanner(userId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAvatar")
    public ResponseEntity<String> getAvatar(@PathVariable @NotBlank String userId) {
        String avatarUrl = userMediaService.getAvatarUrl(userId);
        return ResponseEntity.ok(avatarUrl);
    }

    @GetMapping("/getBanner")
    public ResponseEntity<String> getBanner(@PathVariable @NotBlank String userId) {
        String bannerUrl = userMediaService.getBannerUrl(userId);
        return ResponseEntity.ok(bannerUrl);
    }
}
