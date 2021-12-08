package ru.erminson.lc.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.erminson.lc.service.impl.UserDetailsServiceImpl;

import java.security.Principal;

@RestController
@RequestMapping("profile")
public class ProfileController {
    private final UserDetailsServiceImpl userDetailsService;

    public ProfileController(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping
    public UserDetails profile(Principal principal) {
        String username = principal.getName();
        return userDetailsService.loadUserByUsername(username);
    }
}
