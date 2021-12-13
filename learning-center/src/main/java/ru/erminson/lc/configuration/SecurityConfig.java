package ru.erminson.lc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.erminson.lc.security.CustomBasicAuthenticationEntryPoint;
import ru.erminson.lc.service.impl.UserDetailsServiceImpl;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final CustomBasicAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(
            UserDetailsServiceImpl userDetailsServiceImpl,
            CustomBasicAuthenticationEntryPoint authenticationEntryPoint
    ) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests()
                .antMatchers("/h2-console/**", "/courses/**").permitAll()
                .antMatchers("/users/**").hasRole("ADMIN")
                .antMatchers("/profile/**").authenticated()
                .antMatchers("/students/**").hasAnyRole("TEACHER", "ADMIN")
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint);

        http.headers().frameOptions().disable();

        http.csrf().disable();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
