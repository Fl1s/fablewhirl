package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.service.UserMediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users/{userId}/media")
@RequiredArgsConstructor
public class MediaController {

    private final UserMediaService userMediaService;

    @PatchMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String userId,
                                               @RequestParam("file") MultipartFile file) {
        try {
            String avatarUrl = userMediaService.uploadAvatar(userId, file);
            return ResponseEntity.ok(avatarUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file format or size.");
        }
    }

    @PatchMapping("/banner")
    public ResponseEntity<String> uploadBanner(@PathVariable String userId,
                                               @RequestParam("file") MultipartFile file) {
        try {
            String bannerUrl = userMediaService.uploadBanner(userId, file);
            return ResponseEntity.ok(bannerUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file format or size.");
        }
    }

    @GetMapping("/avatar")
    public ResponseEntity<String> getAvatar(@PathVariable String userId) {
        String avatarUrl = userMediaService.getAvatarUrl(userId);
        return ResponseEntity.ok(avatarUrl);
    }

    @GetMapping("/banner")
    public ResponseEntity<String> getBanner(@PathVariable String userId ) {
        String bannerUrl = userMediaService.getBannerUrl(userId);
        return ResponseEntity.ok(bannerUrl);
    }
}




