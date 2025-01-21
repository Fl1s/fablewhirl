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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMediaRepository userMediaRepository;
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
    public UserCreateEditDto updateUser(String id, UserCreateEditDto userRegistrationDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userEntity.setUsername(userRegistrationDto.getUsername());
        userEntity.setEmail(userRegistrationDto.getEmail());
        userEntity.setBio(userRegistrationDto.getBio());
        userEntity.setUpdatedDate(LocalDateTime.now());

        userRepository.save(userEntity);

        return new UserCreateEditDto(userEntity.getUsername(), userEntity.getEmail(),
                userEntity.getBio(), userEntity.getRoles());
    }
}


