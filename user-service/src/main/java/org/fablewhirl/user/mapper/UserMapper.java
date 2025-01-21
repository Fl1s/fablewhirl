package org.fablewhirl.user.mapper;
import org.fablewhirl.user.dto.UserDto;
import org.fablewhirl.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface UserMapper {
    UserEntity toEntity(UserDto userDto);
    UserDto toDto(UserEntity userEntity);

    void updateEntityFromDto(UserDto userDto,@MappingTarget UserEntity userEntity);
}
