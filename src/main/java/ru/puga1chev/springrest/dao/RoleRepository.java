package ru.puga1chev.springrest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.puga1chev.springrest.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {

}
