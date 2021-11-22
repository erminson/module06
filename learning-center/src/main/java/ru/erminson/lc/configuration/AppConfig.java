package ru.erminson.lc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.utils.CourseRepositoryYamlInitializer;
import ru.erminson.lc.utils.RecordBookRepositoryInitializer;
import ru.erminson.lc.utils.StudentRepositoryYamlInitializer;

@Slf4j
@Configuration
public class AppConfig {
    @Bean("courseRepositoryYaml")
    public CourseRepository createCourseRepository() {
        return CourseRepositoryYamlInitializer.create();
    }

    @Bean("studentRepositoryYaml")
    public StudentRepository createStudentRepository() {
        return StudentRepositoryYamlInitializer.create();
    }

    @Bean("recordBookRepositoryYaml")
    public RecordBookRepository createRecordBookRepository(
            @Qualifier("studentRepositoryJdbc") StudentRepository studentRepository
    ) {
        return RecordBookRepositoryInitializer.create(studentRepository);
    }
}
