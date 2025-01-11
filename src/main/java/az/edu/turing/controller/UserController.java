package az.edu.turing.controller;

import az.edu.turing.model.dto.UserDto;
import az.edu.turing.model.dto.request.CreateUserRequest;
import az.edu.turing.model.dto.request.UpdateUserRequest;
import az.edu.turing.model.enums.UserStatus;
import az.edu.turing.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@Validated
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Set<UserDto>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody CreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.create(request));
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getByUserName (@Email @PathVariable("username") String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@PathVariable("id") long id, @Valid @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.update(id, request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserDto> updateStatus(@PathVariable long id, @RequestParam @NotNull UserStatus status) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.updateStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
