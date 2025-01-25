package org.fablewhirl.user.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.service.UserMediaService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users/{userId}")
@RequiredArgsConstructor
public class MediaController {

    private final UserMediaService userMediaService;

    @PutMapping("/uploadAvatar")
    public ResponseEntity<Void> uploadAvatar(
            @PathVariable @NotBlank String userId,
            @RequestParam("file") @NotNull @Size(max = 5 * 1024 * 1024) MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("File must be an image");
        }

        userMediaService.uploadAvatar(userId, file);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/uploadBanner")
    public ResponseEntity<Void> uploadBanner(
            @PathVariable @NotBlank String userId,
            @RequestParam("file") @NotNull @Size(max = 10 * 1024 * 1024) MultipartFile file) throws IOException {

        if (!file.getContentType().startsWith("image")) {
            throw new IllegalArgumentException("File must be an image");
        }

        userMediaService.uploadBanner(userId, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getAvatar")
    public ResponseEntity<ByteArrayResource> getAvatar(@PathVariable @NotBlank String userId) {
        byte[] imageData = userMediaService.getAvatar(userId);
        ByteArrayResource resource = new ByteArrayResource(imageData);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }

    @GetMapping("/getBanner")
    public ResponseEntity<ByteArrayResource> getBanner(@PathVariable @NotBlank String userId) {
        byte[] imageData = userMediaService.getBanner(userId);
        ByteArrayResource resource = new ByteArrayResource(imageData);

        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .body(resource);
    }
}


