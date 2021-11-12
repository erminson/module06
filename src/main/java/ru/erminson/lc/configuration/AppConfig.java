package ru.erminson.lc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.utils.CourseRepositoryYamlInitializer;
import ru.erminson.lc.utils.RecordBookRepositoryInitializer;
import ru.erminson.lc.utils.StudentRepositoryYamlInitializer;

@Slf4j
@Configuration
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
