package org.fablewhirl.user.controller;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserMediaDto;
import org.fablewhirl.user.service.UserMediaService;
import org.fablewhirl.user.service.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/media")
    public ResponseEntity<ByteArrayResource> getUserMedia(@PathVariable String userId) {
        byte[] imageData = userMediaService.getUserMedia(userId);

        if (imageData == null) {
            return ResponseEntity.notFound().build();
        }

        ByteArrayResource resource = new ByteArrayResource(imageData);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"user_media.png\"");
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE);

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(imageData.length)
                .body(resource);
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
