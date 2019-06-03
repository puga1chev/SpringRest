package ru.puga1chev.springrest.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.puga1chev.springrest.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserCustomRepository {

    private static final String SELECT_USER_BY_LOGIN =
            "SELECT " +
                    "user_id AS id," +
                    "username," +
                    "login," +
                    "pass " +
                    " FROM users u WHERE u.login = :login";

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepositoryImpl(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findByLoginJDBC(String login) {

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("login", login);

        List<User> searchResults = jdbcTemplate.query(
                SELECT_USER_BY_LOGIN,
                queryParams,
                new BeanPropertyRowMapper<>(User.class)
        );

        return searchResults;

    }
}
