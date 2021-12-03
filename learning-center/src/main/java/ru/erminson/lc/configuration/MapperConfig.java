package ru.erminson.lc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.mappers.StudentRowMapper;

@Configuration
public class MapperConfig {
    @Bean
    public StudentRowMapper studentRowMapper() {
        return new StudentRowMapper();
    }
}
