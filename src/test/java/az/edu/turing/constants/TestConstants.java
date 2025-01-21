package az.edu.turing.constants;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.dto.request.CreateUserRequest;
import az.edu.turing.model.dto.request.UpdateUserRequest;
import az.edu.turing.model.enums.UserStatus;

public interface TestConstants {

    long ID_1 = 1L;
    long ID_2 = 2L;
    long ID_3 = 3L;
    String USERNAME = "root@gmail.com";
    String USERNAME_2 = "root_2@gmail.com";
    String USERNAME_3 = "root_3@gmail.com";
    String UPDATED_USERNAME = "test@gmail.com";
    String PASSWORD = "Root123!";
    String UPDATED_PASSWORD = "Test123!";
    String BASE_URL = "/api/v1/users";
    UserStatus STATUS = UserStatus.ACTIVATE;

    UserEntity USER_ENTITY_1 = UserEntity.builder()
            .id(ID_1)
            .username(USERNAME)
            .password(PASSWORD)
            .status(STATUS)
            .build();

    UserEntity USER_ENTITY_2 = UserEntity.builder()
            .id(ID_2)
            .username(USERNAME_2)
            .password(PASSWORD)
            .status(STATUS)
            .build();

    UserEntity USER_ENTITY_3 = UserEntity.builder()
            .id(ID_3)
            .username(USERNAME_3)
            .password(PASSWORD)
            .status(STATUS)
            .build();

    UserEntity UPDATED_USER_ENTITY = UserEntity.builder()
            .id(ID_1)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .status(STATUS)
            .build();


    CreateUserRequest CREATE_USER_REQUEST = CreateUserRequest.builder()
            .username(USERNAME)
            .password(PASSWORD)
            .confirmPassword(PASSWORD)
            .build();

    UpdateUserRequest UPDATE_USER_REQUEST = UpdateUserRequest.builder()
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .build();

    UserDto USER_DTO = UserDto.builder()
            .id(ID_1)
            .username(USERNAME)
            .password(PASSWORD)
            .status(STATUS)
            .build();

    UserDto UPDATED_USER_DTO = UserDto.builder()
            .id(ID_1)
            .username(UPDATED_USERNAME)
            .password(UPDATED_PASSWORD)
            .status(STATUS)
            .build();

}
