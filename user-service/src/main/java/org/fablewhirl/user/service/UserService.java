package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserDto;
import org.fablewhirl.user.dto.UserMediaDto;
import org.fablewhirl.user.dto.UserRegistrationDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.entity.UserMediaEntity;
import org.fablewhirl.user.mapper.UserMapper;
import org.fablewhirl.user.mapper.UserRegistrationMapper;
import org.fablewhirl.user.repository.UserMediaRepository;
import org.fablewhirl.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMediaRepository userMediaRepository;
    private final UserMapper userMapper;
    private final UserRegistrationMapper userRegMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRegistrationDto userDto) {
        UserEntity user = userRegMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public UserDto getUserById(Long id) {
        return userMapper.toDto(userRepository.findById(id).orElse(null));
    }

    @Transactional
    public UserDto updateUser(Long id, UserRegistrationDto userRegistrationDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userEntity.setUsername(userRegistrationDto.getUsername());
        userEntity.setEmail(userRegistrationDto.getEmail());
        userEntity.setBio(userRegistrationDto.getBio());
        userEntity.setUpdatedDate(LocalDateTime.now());

        userRepository.save(userEntity);

        return new UserDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getBio(), userEntity.getRoles(), userEntity.getCreatedDate(), userEntity.getUpdatedDate());
    }

    @Transactional
    public UserMediaDto updateUserMedia(String userId, UserMediaDto userMediaDto) {
        Optional<UserMediaEntity> userMediaEntityOptional = userMediaRepository.findById(userId);
        UserMediaEntity userMediaEntity;

        userMediaEntity = userMediaEntityOptional.orElseGet(UserMediaEntity::new);

        userMediaEntity.setAvatarUrl(userMediaDto.getAvatarUrl());
        userMediaEntity.setBannerUrl(userMediaDto.getBannerUrl());

        userMediaRepository.save(userMediaEntity);

        return new UserMediaDto(userMediaEntity.getAvatarUrl(), userMediaEntity.getBannerUrl());
    }
}


