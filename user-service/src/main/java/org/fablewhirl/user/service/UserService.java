package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserDto;
import org.fablewhirl.user.dto.UserMediaDto;
import org.fablewhirl.user.dto.UserRegistrationDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.entity.UserMediaEntity;
import org.fablewhirl.user.repository.UserMediaRepository;
import org.fablewhirl.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMediaRepository userMediaRepository;

    @Transactional
    public UserDto registerUser(UserRegistrationDto userRegistrationDTO) {
        String hashedPassword = hashPassword(userRegistrationDTO.getPassword());

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(userRegistrationDTO.getUsername());
        userEntity.setEmail(userRegistrationDTO.getEmail());
        userEntity.setPassword(hashedPassword);
        userEntity.setBio(userRegistrationDTO.getBio());
        userEntity.setRoles("USER");
        userEntity.setCreatedDate(LocalDateTime.now());
        userEntity.setUpdatedDate(LocalDateTime.now());

        userRepository.save(userEntity);

        return new UserDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getBio(), userEntity.getRoles(), userEntity.getCreatedDate(), userEntity.getUpdatedDate());
    }

    public UserDto getUserById(Long id) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserDto(userEntity.getId(), userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getBio(), userEntity.getRoles(), userEntity.getCreatedDate(), userEntity.getUpdatedDate());
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

        if (userMediaEntityOptional.isPresent()) {
            userMediaEntity = userMediaEntityOptional.get();
        } else {
            userMediaEntity = new UserMediaEntity();
        }

        userMediaEntity.setAvatarUrl(userMediaDto.getAvatarUrl());
        userMediaEntity.setBannerUrl(userMediaDto.getBannerUrl());

        userMediaRepository.save(userMediaEntity);

        return new UserMediaDto(userMediaEntity.getAvatarUrl(), userMediaEntity.getBannerUrl());
    }

    private String hashPassword(String password) {
        return org.springframework.
                security.crypto.bcrypt.BCrypt.
                hashpw(password, org.springframework.security.crypto.bcrypt.BCrypt.gensalt());
    }
}

