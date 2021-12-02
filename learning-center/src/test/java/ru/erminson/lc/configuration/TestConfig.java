package ru.erminson.lc.configuration;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ResourceLoader;
import ru.erminson.lc.utils.SqlFactory;

@TestConfiguration
public class TestConfig {
    @Bean
    public SqlFactory sqlFactory(ResourceLoader resourceLoader) {
        return new SqlFactory(resourceLoader);
    }
}
