package az.edu.turing.service;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.PostgresUserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.enums.UserStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static az.edu.turing.constants.TestConstants.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private PostgresUserRepository userRepository;

    @Spy
    UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void findAll_Should_ReturnSuccess() {

        given(userRepository.findAll()).willReturn(List.of(USER_ENTITY_1));

        Set<UserDto> users = userService.findAll();
        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(Set.of(USER_DTO), users);

        then(userRepository).should(times(1)).findAll();
    }

    @Test
    void findByUsername_Should_ReturnSuccess() {
        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.of(USER_ENTITY_1));

        UserDto result = userService.findByUsername(USERNAME);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(USER_DTO, result);

        then(userRepository).should(times(1)).findByUsername(USERNAME);
    }

    @Test
    void findByUsername_Should_ThrowNotFoundException_When_UsernameNotFound() {
        given(userRepository.findByUsername(USERNAME)).willReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userService.findByUsername(USERNAME)
        );

        Assertions.assertEquals("There is not user with username " + USERNAME, exception.getMessage());
        then(userRepository).should(times(1)).findByUsername(USERNAME);
    }

    @Test
    void create_Should_ReturnSuccess() {

        given(userRepository.save(userMapper.toEntity(CREATE_USER_REQUEST))).willReturn(USER_ENTITY_1);

        UserDto result = userService.create(CREATE_USER_REQUEST);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(USER_DTO, result);

        then(userRepository).should(times(1)).save(userMapper.toEntity(CREATE_USER_REQUEST));
    }

    @Test
    void create_Should_ThrowAlreadyExistsException_When_UserAlreadyExists() {

        given(userRepository.existsByUsername(USERNAME)).willReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class,
                () -> userService.create(CREATE_USER_REQUEST));
        Assertions.assertEquals("user already exists with this username " + USERNAME, exception.getMessage());

        then(userRepository).should(never()).save(any());
    }

    @Test
    void create_Should_ThrowsInvalidInputException_When_PasswordDontMatch() {

        final String wrong_password = "nothing";
        CREATE_USER_REQUEST.setConfirmPassword(wrong_password);
        InvalidInputException exception = Assertions.assertThrows(InvalidInputException.class,
                () -> userService.create(CREATE_USER_REQUEST)
        );
        Assertions.assertEquals("Passwords do not match", exception.getMessage());

        then(userRepository).should(never()).save(any());
//        then(userRepository).should(never()).existsByUsername(anyString());
    }

    @Test
    void update_Should_ReturnSuccess() {

        given(userRepository.findById(ID_1)).willReturn(Optional.of(USER_ENTITY_1));
        given(userRepository.existsByUsername(UPDATED_USERNAME)).willReturn(false);
        given(userRepository.save(any())).willReturn(UPDATED_USER_ENTITY);

        UserDto result = userService.update(ID_1, UPDATE_USER_REQUEST);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(UPDATED_USERNAME, result.getUsername());
        Assertions.assertEquals(UPDATED_USER_ENTITY, userMapper.toEntity(result));

        then(userRepository).should(times(1)).findById(ID_1);
        then(userRepository).should(times(1)).save(UPDATED_USER_ENTITY);
    }

    @Test
    void updateUser_Should_ThrowAlreadyExistsException_When_UserNotFound() {

        given(userRepository.findById(ID_1)).willReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.update(ID_1, UPDATE_USER_REQUEST)
        );

        Assertions.assertEquals("There is not user with id " + ID_1, exception.getMessage());

        then(userRepository).should(times(1)).findById(ID_1);
        then(userRepository).should(never()).existsByUsername(UPDATED_USERNAME);
        then(userRepository).should(never()).save(UPDATED_USER_ENTITY);
    }

    @Test
    void updateUser_Should_ThrowAlreadyExistsException_When_UsernameAlreadyExists() {

        given(userRepository.findById(ID_1)).willReturn(Optional.of(USER_ENTITY_1));
        given(userRepository.existsByUsername(UPDATED_USERNAME)).willReturn(true);

        AlreadyExistsException exception = Assertions.assertThrows(AlreadyExistsException.class,
                () -> userService.update(ID_1, UPDATE_USER_REQUEST)
        );

        Assertions.assertEquals("user already exists with this username " + UPDATED_USERNAME, exception.getMessage());

        then(userRepository).should(times(1)).findById(ID_1);
        then(userRepository).should(times(1)).existsByUsername(UPDATED_USERNAME);
        then(userRepository).should(never()).save(UPDATED_USER_ENTITY);
    }

    @Test
    void updateStatus_Should_ReturnSuccess() {

        UserEntity updatedUserEntity = UserEntity.builder()
                .id(ID_2)
                .username(USERNAME)
                .password(PASSWORD)
                .status(UserStatus.INACTIVATE)
                .build();

        given(userRepository.findById(ID_2)).willReturn(Optional.of(USER_ENTITY_2));
        given(userRepository.save(any())).willReturn(updatedUserEntity);

        UserDto result = userService.updateStatus(ID_2, UserStatus.INACTIVATE);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(UserStatus.INACTIVATE, result.getStatus());

        then(userRepository).should(times(1)).findById(ID_2);
        then(userRepository).should(times(1)).save(any());
    }

    @Test
    void updateStatus_Should_ThrowsNotFoundException_When_UserNotFound() {

        given(userRepository.findById(ID_3)).willReturn(Optional.empty());

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.updateStatus(ID_3, UserStatus.INACTIVATE)
        );

        Assertions.assertEquals("There is not user with id " + ID_3, exception.getMessage());

        then(userRepository).should(times(1)).findById(ID_3);
        then(userRepository).should(never()).save(any());
    }

    @Test
    void deleteById_Should_ReturnSuccess() {

        given(userRepository.findById(ID_3)).willReturn(Optional.of(USER_ENTITY_3));
        given(userRepository.existsById(ID_3)).willReturn(true);

        userService.deleteById(ID_3);
        Assertions.assertEquals(UserStatus.DELETED, USER_ENTITY_3.getStatus());

        then(userRepository).should(times(1)).findById(ID_3);
        then(userRepository).should(times(1)).save(USER_ENTITY_3);
    }

    @Test
    void deleteById_Should_ThrowUserNotFoundException() {

        given(userRepository.existsById(ID_3)).willReturn(false);

        NotFoundException exception = Assertions.assertThrows(NotFoundException.class,
                () -> userService.deleteById(ID_3)
        );
        Assertions.assertEquals("There is not user with id " + ID_3, exception.getMessage());

        then(userRepository).should(never()).save(any());
        then(userRepository).should(never()).findById(ID_3);
        then(userRepository).should(times(1)).existsById(ID_3);
    }
}

