package ru.erminson.lc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erminson.lc.model.User;
import ru.erminson.lc.service.UserService;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    List<User> users() {
        return userService.findAll();
    }

    @GetMapping("/{login}")
    User users(@PathVariable String login) {
        return userService.findByLogin(login);
    }
}
