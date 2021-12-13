package ru.erminson.lc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.mapper.StudentRowMapper;
import ru.erminson.lc.mapper.UserExtractor;

@Configuration
public class MapperConfig {
    @Bean
    public StudentRowMapper studentRowMapper() {
        return new StudentRowMapper();
    }

    @Bean
    public UserExtractor userRowMapper() {
        return new UserExtractor();
    }
}
