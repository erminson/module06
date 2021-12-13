package ru.erminson.lc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.erminson.lc.model.dto.request.ProfileRequest;
import ru.erminson.lc.service.impl.UserDetailsServiceImpl;

import javax.validation.Valid;
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

    @PutMapping
    public ResponseEntity<String> edit(@RequestBody @Valid ProfileRequest profileRequest, Principal principal) {
        String login = principal.getName();
        userDetailsService.edit(login, profileRequest);

        return ResponseEntity.ok("Profile updated");
    }
}
