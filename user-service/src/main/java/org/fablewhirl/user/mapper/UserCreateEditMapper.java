package org.fablewhirl.user.mapper;
import org.fablewhirl.user.dto.UserCreateEditDto;
import org.fablewhirl.user.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(
        componentModel = "spring"
)
public interface UserCreateEditMapper {
    UserEntity toEntity(UserCreateEditDto userCreateEditDto);
    UserCreateEditDto toDto(UserEntity userEntity);

    void updateEntityFromDto(UserCreateEditDto userCreateEditDto, @MappingTarget UserEntity userEntity);

}
