package ru.erminson.lc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.erminson.lc.model.User;
import ru.erminson.lc.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

//@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getName();
        String password = authentication.getCredentials().toString();
        String encodedPassword = passwordEncoder.encode(password);

        Optional<User> user = userRepository.findByLoginAndPassword(login, encodedPassword);

        return user.map(u ->  new UsernamePasswordAuthenticationToken(login, password, new ArrayList<>()))
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found: " + login));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
