package az.edu.turing.domain.repository;

import az.edu.turing.domain.entity.UserEntity;
import az.edu.turing.mapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
@Repository
public class UserRepository {

    public final JdbcTemplate jdbcTemplate;
    public final UserRowMapper rowMapper;

    private final AtomicLong id = new AtomicLong();
    private static final Set<UserEntity> USERS = new HashSet<>();

    public Collection<UserEntity> findAll() {
        String querySelect = "SELECT* FROM user_table;";
        return jdbcTemplate.query(querySelect, rowMapper);
    }

    public boolean existsByUsername(String username) {
        String querySelect = "SELECT COUNT(*) FROM user_table WHERE username = ?";
        return jdbcTemplate.queryForObject(querySelect, Integer.class, username) > 0;
    }

    public UserEntity save(UserEntity userEntity) {
        if (userEntity.getId() == null) {
            String queryInsert = """
                    INSERT INTO user_table (username, password)
                    VALUES (?,?);
                    """;
            jdbcTemplate.update(queryInsert, userEntity.getUsername(), userEntity.getPassword());
//            userEntity.setId(id.incrementAndGet());
        } else {
            String queryUpdate = """
                    UPDATE user_table SET username = ?,
                    password = ?
                    WHERE id = ?;
                    """;
            jdbcTemplate.update(queryUpdate,
                    userEntity.getUsername(),
                    userEntity.getPassword(),
                    userEntity.getId());
//            deleteById(userEntity.getId());
        }
//        USERS.add(userEntity);
        return userEntity;
    }

    public Optional<UserEntity> finByUsername(String username) {
        String querySelect = "SELECT * FROM user_table WHERE username = ?";
        List<UserEntity> results = jdbcTemplate.query(querySelect, rowMapper, username);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public Optional<UserEntity> findById(long id) {
        String querySelect = "SELECT* FROM user_table WHERE id = ?";
        List<UserEntity> results = jdbcTemplate.query(querySelect, rowMapper, id);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public boolean existsById(long id) {
        String querySelect = "SELECT COUNT(*) FROM user_table WHERE id = ?";
        return jdbcTemplate.queryForObject(querySelect, Integer.class, id) > 0;
    }

    public void deleteById(long id) {
        String queryDelete = """
                UPDATE user_table SET status = ?
                WHERE id = ?;
                """;
        jdbcTemplate.update(queryDelete, id);
    }
}
