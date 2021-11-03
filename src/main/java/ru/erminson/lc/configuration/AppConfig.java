package ru.erminson.lc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.utils.CourseRepositoryYamlInitializer;
import ru.erminson.lc.utils.RecordBookRepositoryInitializer;
import ru.erminson.lc.utils.StudentRepositoryYamlInitializer;

@Configuration
@ComponentScan("ru.erminson.lc")
@Import(TopicScoreConfig.class)
public class AppConfig {
    @Bean
    public CourseRepository createCourseRepository() {
        return CourseRepositoryYamlInitializer.create();
    }

    @Bean
    public StudentRepository createStudentRepository() {
        return StudentRepositoryYamlInitializer.create();
    }

    @Bean
    @Autowired
    public RecordBookRepository createRecordBookRepository(StudentRepository studentRepository) {
        return RecordBookRepositoryInitializer.create(studentRepository);
    }
}
