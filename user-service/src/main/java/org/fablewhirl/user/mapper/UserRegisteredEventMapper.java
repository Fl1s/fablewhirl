package org.fablewhirl.user.mapper;

import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.UserRegisteredEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface UserRegisteredEventMapper {
    UserEntity toEntity(UserRegisteredEvent userRegisteredEvent);
    UserRegisteredEvent toDto(UserEntity userEntity);

    void updateEntityFromDto(UserRegisteredEvent userRegisteredEvent, @MappingTarget UserEntity userEntity);

}
