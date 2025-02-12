package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.mapper.UserReadMapper;
import org.fablewhirl.user.mapper.UserRegistrationEventMapper;
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
    private final UserRegistrationEventMapper userRegistrationEventMapper;
    private final UserReadMapper userReadMapper;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(UserRegistrationEvent userDto) {
        if (existsByUsernameOrEmail(userDto.getUsername(), userDto.getEmail())) {
            throw new IllegalArgumentException("User with the same username or email already exists");
        }

        UserEntity user = userRegistrationEventMapper.toEntity(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userRepository.save(user);
    }

    public boolean existsById(String userId) {
        return userRepository.existsById(userId);
    }

    public boolean existsByUsernameOrEmail(String username, String email) {
        return userRepository.existsByUsernameOrEmail(username, email);
    }

    public Optional<UserReadDto> getUserById(String userId) {
        return userRepository.findById(userId)
                .map(userReadMapper::toDto);
    }

    public List<UserReadDto> getAll() {
        return userRepository.findAll().stream()
                .map(userReadMapper::toDto)
                .toList();
    }

    @Transactional
    public UserReadDto updateUser(String userId, UserCreateEditDto userDto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("[User not found...]"));

        Optional.ofNullable(userDto.getUsername())
                .ifPresent(userEntity::setUsername);
        Optional.ofNullable(userDto.getPassword())
                .ifPresent(password -> userEntity.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(userDto.getEmail())
                .ifPresent(userEntity::setEmail);
        Optional.ofNullable(userDto.getBio())
                .ifPresent(userEntity::setBio);

        userEntity.setUpdatedDate(LocalDateTime.now());

        UserEntity updatedEntity = userRepository.save(userEntity);
        return userReadMapper.toDto(updatedEntity);
    }

    @Transactional
    public void deleteUser(String userId) {
        if (!existsById(userId)) {
            throw new IllegalArgumentException("[User not found...]");
        }
        userRepository.deleteById(userId);
    }
}



