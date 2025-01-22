package org.fablewhirl.user.service;

import jakarta.validation.constraints.NotNull;
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
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);

        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
            mediaEntity.setUserId(userId);
        }
        mediaEntity.setAvatar(avatarFile.getBytes());

        userMediaRepository.save(mediaEntity);
    }

    public void uploadBanner(String userId, MultipartFile bannerFile) throws IOException {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);

        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
            mediaEntity.setUserId(userId);
        }
        mediaEntity.setBanner(bannerFile.getBytes());

        userMediaRepository.save(mediaEntity);
    }

    public byte[] getUserMedia(String userId) {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);

        if (mediaEntity == null) {
            throw new IllegalArgumentException("Media not found for user: " + userId);
        }

        byte[] avatarData = mediaEntity.getAvatar();
        byte[] bannerData = mediaEntity.getBanner();

        return avatarData != null ? avatarData : bannerData;
    }
    @NotNull
    public byte[] getAvatar(String userId) {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        return mediaEntity.getAvatar();
    }
    @NotNull
    public byte[] getBanner(String userId) {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        return mediaEntity.getBanner();
    }
}

