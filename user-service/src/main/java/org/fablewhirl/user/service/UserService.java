package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.mapper.UserCreateEditMapper;
import org.fablewhirl.user.mapper.UserReadMapper;
import org.fablewhirl.user.mapper.UserRegisteredEventMapper;
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
    private final UserRegisteredEventMapper userRegisteredEventMapper;
    private final UserReadMapper userReadMapper;
    private final PasswordEncoder passwordEncoder;

    public void register(UserRegisteredEvent userDto) {
        UserEntity user = userRegisteredEventMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        userRepository.save(user);
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


