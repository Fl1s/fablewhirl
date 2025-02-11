package org.fablewhirl.user.repository;


import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUsernameOrEmail(String username, String email);

    Optional<UserEntity> findByUsername(String username);
}