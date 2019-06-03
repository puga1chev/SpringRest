package ru.puga1chev.springrest.dao;

import ru.puga1chev.springrest.entity.User;

import java.util.List;

public interface UserCustomRepository {

    List<User> findByLoginJDBC(String login);
}
