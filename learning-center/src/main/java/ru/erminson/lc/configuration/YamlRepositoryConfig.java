package ru.erminson.lc.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.utils.CourseRepositoryYamlInitializer;
import ru.erminson.lc.utils.RecordBookRepositoryInitializer;
import ru.erminson.lc.utils.StudentRepositoryYamlInitializer;

@Configuration
@ConditionalOnProperty(value = "db.type", havingValue = "yaml")
public class YamlRepositoryConfig {
    @Bean
    public CourseRepository createCourseRepository() {
        return CourseRepositoryYamlInitializer.create();
    }

    @Bean
    public StudentRepository createStudentRepository() {
        return StudentRepositoryYamlInitializer.create();
    }

    @Bean
    public RecordBookRepository createRecordBookRepository(StudentRepository studentRepository) {
        return RecordBookRepositoryInitializer.create(studentRepository);
    }
}
