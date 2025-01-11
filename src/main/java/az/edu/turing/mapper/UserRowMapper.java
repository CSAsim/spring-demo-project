package az.edu.turing.mapper;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.model.enums.UserStatus;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        return UserEntity.builder()
                .id(rs.getLong("id"))
                .username(rs.getString("username"))
                .password(rs.getString("password"))
                .status(UserStatus.valueOf(rs.getString("status")))
                .build();
    }
}
