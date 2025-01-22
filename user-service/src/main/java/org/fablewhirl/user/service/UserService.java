package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.mapper.UserMapper;
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
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserCreateEditDto userDto) {
        UserEntity user = userMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public UserCreateEditDto getUserById(String id) {
        return userMapper.toDto(
                userRepository.findById(id)
                        .orElse(null));
    }

    @Transactional
    public void updateUser(String id, UserCreateEditDto userRegistrationDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Optional.ofNullable(userRegistrationDto.getUsername())
                .ifPresent(userEntity::setUsername);
        Optional.ofNullable(userRegistrationDto.getPassword())
                .ifPresent(userEntity::setPassword);
        Optional.ofNullable(userRegistrationDto.getEmail())
                .ifPresent(userEntity::setEmail);
        Optional.ofNullable(userRegistrationDto.getBio())
                .ifPresent(userEntity::setBio);
        userEntity.setUpdatedDate(LocalDateTime.now());

        userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}


