package ru.erminson.lc.utils;

import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.service.CourseService;
import ru.erminson.lc.service.RecordBookService;
import ru.erminson.lc.service.StudentService;
import ru.erminson.lc.service.StudyService;
import ru.erminson.lc.service.impl.CourseServiceImpl;
import ru.erminson.lc.service.impl.RecordBookServiceImpl;
import ru.erminson.lc.service.impl.StudentServiceImpl;
import ru.erminson.lc.service.impl.StudyServiceImpl;

public class StudyServiceFactory {
    private StudyServiceFactory() {
        throw new IllegalStateException("StudyServiceFactory utility class");
    }

    public static StudyService createStudyService() {
        StudentRepository studentRepository = StudentRepositoryYamlInitializer.create();
        StudentService studentService = new StudentServiceImpl(studentRepository);

        RecordBookRepository recordBookRepository = RecordBookRepositoryInitializer.create(studentRepository);
        RecordBookService recordBookService = new RecordBookServiceImpl(recordBookRepository);

        CourseRepository courseRepository = CourseRepositoryYamlInitializer.create();
        CourseService courseService = new CourseServiceImpl(courseRepository);

        return new StudyServiceImpl(studentService, recordBookService, courseService);
    }
}
