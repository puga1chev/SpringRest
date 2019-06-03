package ru.puga1chev.springrest.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import ru.puga1chev.springrest.entity.*;
import ru.puga1chev.springrest.service.ObjectService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("admin")
@EnableAutoConfiguration
@ComponentScan
public class AdminController {

    private final static Logger logger = Logger.getLogger(AdminController.class);

    @Autowired
    private ObjectService<User> userService;
    @Autowired
    private ObjectService<Role> roleService;

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String UsersViewPage(Model model) {

        List<User> users = userService.findAll("id");
        model.addAttribute("users", users);
        model.addAttribute("new_user", new User());
        model.addAttribute("action_add", "/admin/add");
        model.addAttribute("action_edit", "/admin/edit");
        model.addAttribute("roles", roleService.findAll("id"));

        return "admin_panel";
    }

    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String editPage(Model model, @PathVariable(name = "id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("user_manage");

        User user = userService.getById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        model.addAttribute("user", user);
        model.addAttribute("action", "/admin/edit");
        model.addAttribute("roles", roleService.findAll("id"));

        return "user_manage";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addPage(Model model) {

        model.addAttribute("user", new User());
        model.addAttribute("action", "/admin/add");
        model.addAttribute("roles", roleService.findAll("id"));

        return "user_manage";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView deletePage(@PathVariable("id") Long id) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");

        userService.deleteById(id);

        return modelAndView;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ModelAndView addUserPage(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "login") String login,
            @RequestParam(name = "pass") String pass,
            @RequestParam(name = "getAllRoles") String[] pointedRoles,
            ModelMap model
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");

        // fill user object from form fields
        List<Role> roles = new ArrayList<>();
        for (String pointedRole: pointedRoles) {
            roles.add(
                    roleService.getById(Long.parseLong(pointedRole))
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + pointedRole))
            );
        }

        // if id set not null that will be error
        User newUser = new User(null, username, login, pass, roles);
        try {
            userService.save(newUser);
        } catch (Exception e) {
            logger.error("Попытка записать пользователя", e);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public ModelAndView editUserPage(
            @RequestParam(name = "id") String id,
            @RequestParam(name = "username") String username,
            @RequestParam(name = "login") String login,
            @RequestParam(name = "pass") String pass,
            @RequestParam(name = "getAllRoles") String[] pointedRoles,
            ModelMap model
    ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("redirect:/admin/users");

        // fill user object from form fields
        User modifiedUser = userService.getById(Long.parseLong(id))
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        modifiedUser.setUsername(username);
        modifiedUser.setLogin(login);
        modifiedUser.setPass(pass);
        List<Role> roles = new ArrayList<>();
        for (String pointedRole: pointedRoles) {
            roles.add(
                    roleService.getById(Long.parseLong(pointedRole))
                            .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + pointedRole))
            );
        }
        modifiedUser.setRoles(roles);
        try {
            userService.save(modifiedUser);
        } catch (Exception e) {
            logger.error("Попытка записать пользователя", e);
        }
        return modelAndView;
    }
}
