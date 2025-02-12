package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.service.UserMediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/users/media")
@RequiredArgsConstructor
public class MediaController {

    private final UserMediaService userMediaService;

    @PatchMapping("/avatar")
    public ResponseEntity<String> uploadAvatar(@AuthenticationPrincipal Jwt jwt,
                                               @RequestParam("file") MultipartFile file) {
        try {
            String avatarUrl = userMediaService.uploadAvatar(jwt.getSubject(), file);
            return ResponseEntity.ok(avatarUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file format or size.");
        }
    }

    @PatchMapping("/banner")
    public ResponseEntity<String> uploadBanner(@AuthenticationPrincipal Jwt jwt,
                                               @RequestParam("file") MultipartFile file) {
        try {
            String bannerUrl = userMediaService.uploadBanner(jwt.getSubject(), file);
            return ResponseEntity.ok(bannerUrl);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid file format or size.");
        }
    }

    @GetMapping("/avatar")
    public ResponseEntity<String> getAvatar(@AuthenticationPrincipal Jwt jwt) {
        String avatarUrl = userMediaService.getAvatarUrl(jwt.getSubject());
        return ResponseEntity.ok(avatarUrl);
    }

    @GetMapping("/banner")
    public ResponseEntity<String> getBanner(@AuthenticationPrincipal Jwt jwt) {
        String bannerUrl = userMediaService.getBannerUrl(jwt.getSubject());
        return ResponseEntity.ok(bannerUrl);
    }
}




