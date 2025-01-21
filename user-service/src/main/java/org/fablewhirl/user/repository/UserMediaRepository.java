package org.fablewhirl.user.repository;

import org.fablewhirl.user.entity.UserMediaEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserMediaRepository extends MongoRepository<UserMediaEntity, String> {
}

