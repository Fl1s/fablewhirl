package org.fablewhirl.user.mapper;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.dto.UserReadDto;
import org.fablewhirl.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface UserReadMapper {
    UserEntity toEntity(UserReadDto userReadDto);
    UserReadDto toDto(UserEntity userEntity);

    void updateEntityFromDto(UserReadDto userReadDto, @MappingTarget UserEntity userEntity);

}
