package org.fablewhirl.user.service;

import org.fablewhirl.user.entity.UserMediaEntity;
import org.fablewhirl.user.repository.UserMediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserMediaServiceTest {

    @Mock
    private UserMediaRepository userMediaRepository;

    @Mock
    private MultipartFile file;

    @InjectMocks
    private UserMediaService userMediaService;

    private UserMediaEntity mediaEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mediaEntity = new UserMediaEntity();
        mediaEntity.setUserId("1");
        mediaEntity.setAvatar(new byte[]{1, 2, 3});
        mediaEntity.setBanner(new byte[]{4, 5, 6});
    }

    @Test
    @DisplayName("Upload avatar - success")
    void uploadAvatar_Success() throws IOException {
        when(userMediaRepository.findByUserId("1")).thenReturn(mediaEntity);
        when(file.getBytes()).thenReturn(new byte[]{7, 8, 9});

        userMediaService.uploadAvatar("1", file);

        verify(userMediaRepository).save(mediaEntity);
        assertArrayEquals(new byte[]{7, 8, 9}, mediaEntity.getAvatar());
    }

    @Test
    @DisplayName("Upload avatar - new user media")
    void uploadAvatar_NewMedia() throws IOException {
        when(userMediaRepository.findByUserId("1")).thenReturn(null);
        when(file.getBytes()).thenReturn(new byte[]{7, 8, 9});

        userMediaService.uploadAvatar("1", file);

        verify(userMediaRepository).save(any(UserMediaEntity.class));
    }

    @Test
    @DisplayName("Get avatar - success")
    void getAvatar_Success() {
        when(userMediaRepository.findByUserId("1")).thenReturn(mediaEntity);

        byte[] result = userMediaService.getAvatar("1");

        assertNotNull(result);
        assertArrayEquals(mediaEntity.getAvatar(), result);
    }

    @Test
    @DisplayName("Get avatar - not found")
    void getAvatar_NotFound() {
        when(userMediaRepository.findByUserId("1")).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userMediaService.getAvatar("1"));

        assertEquals("Avatar not found for user: 1", exception.getMessage());
        verify(userMediaRepository).findByUserId("1");
    }

    @Test
    @DisplayName("Upload banner - success")
    void uploadBanner_Success() throws IOException {
        when(userMediaRepository.findByUserId("1")).thenReturn(mediaEntity);
        when(file.getBytes()).thenReturn(new byte[]{10, 11, 12});

        userMediaService.uploadBanner("1", file);

        verify(userMediaRepository).save(mediaEntity);
        assertArrayEquals(new byte[]{10, 11, 12}, mediaEntity.getBanner());
    }

    @Test
    @DisplayName("Get banner - not found")
    void getBanner_NotFound() {
        when(userMediaRepository.findByUserId("1")).thenReturn(null);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userMediaService.getBanner("1"));

        assertEquals("Banner not found for user: 1", exception.getMessage());
        verify(userMediaRepository).findByUserId("1");
    }
}
