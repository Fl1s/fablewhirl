package org.fablewhirl.user.mapper;

import org.fablewhirl.user.entity.UserEntity;
import org.fablewhirl.user.event.UserRegistrationEvent;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface UserRegistrationEventMapper {
    UserEntity toEntity(UserRegistrationEvent userRegisteredEvent);

    UserRegistrationEvent toDto(UserEntity userEntity);

    void updateEntityFromDto(UserRegistrationEvent userRegisteredEvent, @MappingTarget UserEntity userEntity);

}
