package ru.puga1chev.springrest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.puga1chev.springrest.dao.UserRepository;
import ru.puga1chev.springrest.entity.User;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService implements ObjectService<User> {

    @Autowired
    private UserRepository repository;

    public void save(User user) {
        user = repository.save(user);
    }
    
    public ArrayList<User> findAll(String sortedBy) {
        // todo: sorted
        return (ArrayList<User>) repository.findAll();
    }

    public Optional<User> getById(Long id) {

        return repository.findById(id);
    }

    public void deleteById(Long id) {

         repository.deleteById(id);
    }

    @Override
    public ArrayList<User> findByLogin(String login) {
        return (ArrayList<User>) repository.findByLogin(login);
    }
}
