package ru.erminson.lc.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.repository.impl.CourseRepositoryJdbc;
import ru.erminson.lc.repository.impl.RecordBookRepositoryJdbc;
import ru.erminson.lc.repository.impl.StudentRepositoryJdbc;

import javax.sql.DataSource;

@Configuration
@ConditionalOnProperty(value = "db.type", havingValue = "jdbc")
public class JdbcRepositoryConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public CourseRepository createCourseRepository(JdbcTemplate jdbcTemplate) {
        return new CourseRepositoryJdbc(jdbcTemplate);
    }

    @Bean
    public StudentRepository createStudentRepository(JdbcTemplate jdbcTemplate) {
        return new StudentRepositoryJdbc(jdbcTemplate);
    }

    @Bean
    public RecordBookRepository createRecordBookRepository(JdbcTemplate jdbcTemplate) {
        return new RecordBookRepositoryJdbc(jdbcTemplate);
    }
}
