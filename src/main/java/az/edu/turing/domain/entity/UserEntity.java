package az.edu.turing.domain.entity;

import az.edu.turing.model.enums.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Long id;
    private String username;
    private String password;
    private UserStatus status;
}
