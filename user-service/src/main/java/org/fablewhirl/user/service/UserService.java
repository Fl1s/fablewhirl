package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.mapper.UserCreateEditMapper;
import org.fablewhirl.user.mapper.UserReadMapper;
import org.fablewhirl.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCreateEditMapper userCreateEditMapper;
    private final UserReadMapper userReadMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserCreateEditDto register(UserCreateEditDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail()))
            throw new IllegalArgumentException("Email is already in use.");

        UserEntity user = userCreateEditMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userCreateEditMapper.toDto(userRepository.save(user));
    }

    public UserReadDto getUserById(String id) {
        return userReadMapper.toDto(
                userRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("User not found")));
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserCreateEditDto updateUser(String id, UserCreateEditDto userRegistrationDto) {
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

        return userCreateEditMapper.toDto(userRepository.save(userEntity));
    }

    @Transactional
    public void deleteUser(String id) {
        userRepository.deleteById(id);
    }
}


