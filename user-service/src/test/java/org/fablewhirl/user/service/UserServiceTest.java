package org.fablewhirl.user.service;

import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.mapper.UserCreateEditMapper;
import org.fablewhirl.user.mapper.UserReadMapper;
import org.fablewhirl.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCreateEditMapper userCreateEditMapper;

    @Mock UserReadMapper userReadMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreateEditDto userCreateEditDto;
    private UserReadDto userReadDto;
    private UserEntity userEntity;
    private String userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = "12";
        userCreateEditDto = new UserCreateEditDto("testUser", "test@example.com", "password", "bio");
        userReadDto = new UserReadDto("testUser", "test@example.com", "bio", "USER", LocalDateTime.now(),LocalDateTime.now());

        userEntity = new UserEntity();
        userEntity.setId(userId);
        userEntity.setUsername("testUser");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setBio("bio");
    }

    @Test
    @DisplayName("Register user - success")
    void register_Success() {
        when(userCreateEditMapper.toEntity(userCreateEditDto)).thenReturn(userEntity);
        when(passwordEncoder.encode(userCreateEditDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userCreateEditMapper.toDto(userEntity)).thenReturn(userCreateEditDto);

        UserCreateEditDto result = userService.register(userCreateEditDto);

        assertNotNull(result);
        assertEquals(userCreateEditDto.getUsername(), result.getUsername());
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("Get user by ID - success")
    void getUserById_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userReadMapper.toDto(userEntity)).thenReturn(userReadDto);

        UserReadDto result = userService.getUserById(userId);

        assertNotNull(result);
        assertEquals(userReadDto.getUsername(), result.getUsername());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Get user by ID - not found")
    void getUserById_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
    }


    @Test
    @DisplayName("Get all users - success")
    void getAll_Success() {
        when(userRepository.findAll()).thenReturn(List.of(userEntity));

        List<UserEntity> result = userService.getAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Update user - success")
    void updateUser_Success() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userCreateEditMapper.toDto(userEntity)).thenReturn(userCreateEditDto);

        UserCreateEditDto result = userService.updateUser(userId, userCreateEditDto);

        assertNotNull(result);
        assertEquals(userCreateEditDto.getUsername(), result.getUsername());
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("Update user - not found")
    void updateUser_NotFound() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(userId, userCreateEditDto));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Delete user - success")
    void deleteUser_Success() {
        doNothing().when(userRepository).deleteById(userId);

        assertDoesNotThrow(() -> userService.deleteUser(userId));
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Delete user - not found")
    void deleteUser_NotFound() {
        doThrow(new IllegalArgumentException("User not found")).when(userRepository).deleteById(userId);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(userId));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).deleteById(userId);
    }
}
