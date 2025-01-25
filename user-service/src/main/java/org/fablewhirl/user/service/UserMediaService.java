package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.entity.UserMediaEntity;
import org.fablewhirl.user.repository.UserMediaRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserMediaService {

    private final UserMediaRepository userMediaRepository;

    public void uploadAvatar(String userId, MultipartFile avatarFile) throws IOException {
        if (avatarFile.isEmpty())
            throw new IllegalArgumentException("Invalid file: must be an image.");

        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
            mediaEntity.setUserId(userId);
        }
        mediaEntity.setAvatar(avatarFile.getBytes());
        userMediaRepository.save(mediaEntity);
    }

    public void uploadBanner(String userId, MultipartFile bannerFile) throws IOException {
        if (bannerFile.isEmpty())
            throw new IllegalArgumentException("Invalid file: must be an image.");

        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
            mediaEntity.setUserId(userId);
        }
        mediaEntity.setBanner(bannerFile.getBytes());
        userMediaRepository.save(mediaEntity);
    }

    public byte[] getAvatar(String userId) {
        UserMediaEntity userMedia = userMediaRepository.findByUserId(userId);
        if (userMedia == null || userMedia.getAvatar() == null) {
            throw new IllegalArgumentException("Avatar not found for user: " + userId);
        }
        return userMedia.getAvatar();
    }

    public byte[] getBanner(String userId) {
        UserMediaEntity userMedia = userMediaRepository.findByUserId(userId);
        if (userMedia == null || userMedia.getBanner() == null) {
            throw new IllegalArgumentException("Banner not found for user: " + userId);
        }
        return userMedia.getBanner();
    }
}
