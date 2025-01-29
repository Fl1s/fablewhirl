package org.fablewhirl.user.repository;


import org.fablewhirl.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    boolean existsByUsernameOrEmail(String username, String email);
}