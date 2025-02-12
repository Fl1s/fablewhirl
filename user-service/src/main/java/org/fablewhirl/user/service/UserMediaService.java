package org.fablewhirl.user.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.entity.UserMediaEntity;
import org.fablewhirl.user.repository.UserMediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Service
@RequiredArgsConstructor
public class UserMediaService {

    private final UserMediaRepository userMediaRepository;
    private final MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucketName;

    @Transactional
    public String uploadAvatar(String userId, MultipartFile avatarFile) {
        String filePath = buildFilePath("avatars", userId, avatarFile);
        uploadFileToMinio(filePath, avatarFile);

        return updateUserMedia(userId, mediaEntity -> mediaEntity.setAvatarUrl("/" + bucketName + "/" + filePath))
                .getAvatarUrl();
    }
    @Transactional
    public String uploadBanner(String userId, MultipartFile bannerFile) {
        String filePath = buildFilePath("banners", userId, bannerFile);
        uploadFileToMinio(filePath, bannerFile);

        return updateUserMedia(userId, mediaEntity -> mediaEntity.setBannerUrl("/" + bucketName + "/" + filePath))
                .getBannerUrl();
    }

    public String getAvatarUrl(String userId) {
        UserMediaEntity mediaEntity = getUserMedia(userId);
        if (mediaEntity == null || mediaEntity.getAvatarUrl() == null) {
            throw new IllegalArgumentException("Avatar not found for user: " + userId);
        }
        return mediaEntity.getAvatarUrl();
    }

    public String getBannerUrl(String userId) {
        UserMediaEntity mediaEntity = getUserMedia(userId);
        if (mediaEntity == null || mediaEntity.getBannerUrl() == null) {
            throw new IllegalArgumentException("Banner not found for user: " + userId);
        }
        return mediaEntity.getBannerUrl();
    }
    private void uploadFileToMinio(String filePath, MultipartFile file) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(filePath)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Failed to upload file to MinIO: " + e.getMessage(), e);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("File upload error: " + e.getMessage(), e);
        }
    }

    private String buildFilePath(String type, String userId, MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("File must have a name");
        }
        return String.format("%s/%s/%s", type, userId, file.getOriginalFilename());
    }

    private UserMediaEntity updateUserMedia(String userId, java.util.function.Consumer<UserMediaEntity> updater) {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);

        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
        }

        updater.accept(mediaEntity);
        mediaEntity.setUserId(userId);

        return userMediaRepository.save(mediaEntity);
    }

    private UserMediaEntity getUserMedia(String userId) {
        return userMediaRepository.findByUserId(userId);
    }
}