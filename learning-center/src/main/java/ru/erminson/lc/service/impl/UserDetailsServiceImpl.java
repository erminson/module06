package ru.erminson.lc.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.erminson.lc.model.dto.request.ProfileRequest;
import ru.erminson.lc.model.entity.User;
import ru.erminson.lc.repository.UserRepository;
import ru.erminson.lc.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {
    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public User findByLogin(String login) {
        Optional<User> user = userRepository.findByLogin(login);
        return user.orElseThrow(() -> new UsernameNotFoundException("User not found: " + login));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void edit(String login, ProfileRequest profileRequest) {
        int editedCount = userRepository.edit(login, profileRequest);
        if (editedCount == 0) {
            throw new UsernameNotFoundException("User not found: " + login);
        }
    }
}
