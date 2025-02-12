package org.fablewhirl.user.service;

import lombok.RequiredArgsConstructor;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.fablewhirl.user.mapper.UserReadMapper;
import org.fablewhirl.user.mapper.UserRegisteredEventMapper;
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
    private final UserRegisteredEventMapper userRegisteredEventMapper;
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

    public UserReadDto getUserById(String id) {
        return userRepository.findById(id)
                .map(userReadMapper::toDto)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    @Transactional
    public UserReadDto updateUser(String id, UserCreateEditDto userDto) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

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
    public void deleteUser(String id) {
        if (!existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }
}



