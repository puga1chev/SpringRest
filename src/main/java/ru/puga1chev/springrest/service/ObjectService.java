package ru.puga1chev.springrest.service;

import java.util.ArrayList;
import java.util.Optional;

public interface ObjectService<T> {

    public void save(T obj);
    public ArrayList<T> findAll(String sortedBy);
    public Optional<T> getById(Long id);
    public void deleteById(Long id);
    public ArrayList<T> findByLogin(String login);
}
