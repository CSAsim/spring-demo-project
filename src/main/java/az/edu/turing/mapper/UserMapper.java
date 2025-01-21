package az.edu.turing.mapper;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.dto.request.CreateUserRequest;
import az.edu.turing.model.dto.request.UpdateUserRequest;
import az.edu.turing.model.enums.UserStatus;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDto toDto(UserEntity entity) {
        return UserDto.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .password(entity.getPassword())
                .status(entity.getStatus())
                .build();
    }

    public UserEntity toEntity(UserDto dto) {
        return UserEntity.builder()
                .id(dto.getId())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .status(dto.getStatus())
                .build();
    }

    public UserEntity toEntity(CreateUserRequest request) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .status(UserStatus.ACTIVATE)
                .build();
    }

    public UserEntity toEntity(UpdateUserRequest request) {
        return UserEntity.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();
    }
}
