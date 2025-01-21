package az.edu.turing.service;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.domain.repository.PostgresUserRepository;
import az.edu.turing.exception.AlreadyExistsException;
import az.edu.turing.exception.InvalidInputException;
import az.edu.turing.exception.NotFoundException;
import az.edu.turing.mapper.UserMapper;
import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.dto.request.CreateUserRequest;
import az.edu.turing.model.dto.request.UpdateUserRequest;
import az.edu.turing.model.enums.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Log4j2
@Service
public class UserService {

    private final PostgresUserRepository userRepository;
    private final UserMapper mapper;
    private final UserMapper userMapper;

    public Set<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toSet());
    }

    public UserDto create(CreateUserRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new InvalidInputException("Passwords do not match");
        }
        existsByUserName(request.getUsername());
        UserEntity userEntity = mapper.toEntity(request);
        UserEntity savedUserEntity = userRepository.save(userEntity);
        return mapper.toDto(savedUserEntity);
    }


    public UserDto findByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(mapper::toDto)
                .orElseThrow(() -> new NotFoundException("There is not user with username " + username));
    }

    public UserDto update(long id, UpdateUserRequest request) {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("There is not user with id " + id));
        existsByUserName(request.getUsername());
        userEntity.setUsername(request.getUsername());
        userEntity.setPassword(request.getPassword());
        UserEntity savedUserEntity = userRepository.save(userEntity);
        log.info("User updated: {}", savedUserEntity);
        return mapper.toDto(savedUserEntity);
    }

    public UserDto updateStatus(long id, UserStatus status) {
        return userRepository.findById(id)
                .map(userEntity -> {
                    userEntity.setStatus(status);
                    UserEntity updatedUserEntity = userRepository.save(userEntity);
                    log.info("User status updated to: {} for user: {}", status, updatedUserEntity);
                    return userMapper.toDto(updatedUserEntity);
                })
                .orElseThrow(() -> new NotFoundException("There is not user with id " + id));
    }

    public void deleteById(long id) {
        existsById(id);
        userRepository.findById(id)
                .ifPresent(userEntity -> {
                    userEntity.setStatus(UserStatus.DELETED);
                    userRepository.save(userEntity);
                });
    }

    private void existsById(long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("There is not user with id " + id);
        }
    }

    private void existsByUserName(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new AlreadyExistsException("user already exists with this username " + username);
        }
    }
}
