package org.fablewhirl.user.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.entity.UserMediaEntity;
import org.fablewhirl.user.repository.UserMediaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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

    public void uploadAvatar(String userId, MultipartFile avatarFile) throws IOException {
        String fileName = "avatars/" + userId + "/" + avatarFile.getOriginalFilename();

        uploadToMinio(fileName, avatarFile);

        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
            mediaEntity.setUserId(userId);
        }
        mediaEntity.setAvatarUrl("/" + bucketName + "/" + fileName);
        userMediaRepository.save(mediaEntity);
    }

    public void uploadBanner(String userId, MultipartFile bannerFile) throws IOException {
        String fileName = "banners/" + userId + "/" + bannerFile.getOriginalFilename();

        uploadToMinio(fileName, bannerFile);

        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        if (mediaEntity == null) {
            mediaEntity = new UserMediaEntity();
            mediaEntity.setUserId(userId);
        }
        mediaEntity.setBannerUrl("/" + bucketName + "/" + fileName);
        userMediaRepository.save(mediaEntity);
    }

    public String getAvatarUrl(String userId) {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        if (mediaEntity == null || mediaEntity.getAvatarUrl() == null) {
            throw new IllegalArgumentException("Avatar not found for user: " + userId);
        }
        return mediaEntity.getAvatarUrl();
    }

    public String getBannerUrl(String userId) {
        UserMediaEntity mediaEntity = userMediaRepository.findByUserId(userId);
        if (mediaEntity == null || mediaEntity.getBannerUrl() == null) {
            throw new IllegalArgumentException("Banner not found for user: " + userId);
        }
        return mediaEntity.getBannerUrl();
    }

    private void uploadToMinio(String fileName, MultipartFile file) throws IOException {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );
        } catch (MinioException e) {
            throw new RuntimeException("Failed to upload file to MinIO: " + e.getMessage(), e);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
