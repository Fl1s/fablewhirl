package org.fablewhirl.user.service;

import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.mapper.UserMapper;
import org.fablewhirl.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private UserCreateEditDto userDto;
    private UserEntity userEntity;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        userId = UUID.randomUUID();
        userDto = new UserCreateEditDto("testUser", "test@example.com", "password", "bio");

        userEntity = new UserEntity();
        userEntity.setId(UUID.fromString(userId.toString()));
        userEntity.setUsername("testUser");
        userEntity.setEmail("test@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setBio("bio");
    }

    @Test
    @DisplayName("Register user - success")
    void register_Success() {
        when(userMapper.toEntity(userDto)).thenReturn(userEntity);
        when(passwordEncoder.encode(userDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        UserCreateEditDto result = userService.register(userDto);

        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("Get user by ID - success")
    void getUserById_Success() {
        when(userRepository.findById(userId.toString())).thenReturn(Optional.of(userEntity));
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        UserCreateEditDto result = userService.getUserById(userId.toString());

        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
        verify(userRepository).findById(userId.toString());
    }

    @Test
    @DisplayName("Get user by ID - not found")
    void getUserById_NotFound() {
        when(userRepository.findById(userId.toString())).thenReturn(Optional.empty());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.getUserById(userId.toString()));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId.toString());
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
        when(userRepository.findById(userId.toString())).thenReturn(Optional.of(userEntity));
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userMapper.toDto(userEntity)).thenReturn(userDto);

        UserCreateEditDto result = userService.updateUser(userId.toString(), userDto);

        assertNotNull(result);
        assertEquals(userDto.getUsername(), result.getUsername());
        verify(userRepository).save(userEntity);
    }

    @Test
    @DisplayName("Update user - not found")
    void updateUser_NotFound() {
        when(userRepository.findById(userId.toString())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.updateUser(userId.toString(), userDto));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).findById(userId.toString());
    }

    @Test
    @DisplayName("Delete user - success")
    void deleteUser_Success() {
        doNothing().when(userRepository).deleteById(userId.toString());

        assertDoesNotThrow(() -> userService.deleteUser(userId.toString()));
        verify(userRepository).deleteById(userId.toString());
    }

    @Test
    @DisplayName("Delete user - not found")
    void deleteUser_NotFound() {
        doThrow(new IllegalArgumentException("User not found")).when(userRepository).deleteById(userId.toString());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> userService.deleteUser(userId.toString()));

        assertEquals("User not found", exception.getMessage());
        verify(userRepository).deleteById(userId.toString());
    }
}
