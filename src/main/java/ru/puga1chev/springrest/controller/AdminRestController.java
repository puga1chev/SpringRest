package ru.puga1chev.springrest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import ru.puga1chev.springrest.entity.Role;
import ru.puga1chev.springrest.entity.User;
import ru.puga1chev.springrest.service.ObjectService;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("api")
public class AdminRestController {

    private final static Logger logger = Logger.getLogger(AdminController.class);

    @Autowired
    private ObjectService<User> userService;
    @Autowired
    private ObjectService<Role> roleService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> users() {

        return (List<User>) userService.findAll("id");
    }

    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public List<Role> getAllRoles() {

        return (List<Role>) roleService.findAll("id");
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {

        return userService.getById(id).orElseThrow(() -> new IllegalArgumentException("User didn't find"));
    }

    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public User setUser(@RequestBody User user) {

        // load getAllRoles
        List<Role> findedRoles = user.getRoles().stream().map(
                role -> roleService.getById(role.getId()).get()
        ).collect(Collectors.toList());
        user.setRoles(findedRoles);
        // save user
        userService.save(user);

        return user;
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteUser(@PathVariable Long id) {

        userService.deleteById(id);
        return ResponseEntity.ok(true);
    }
}