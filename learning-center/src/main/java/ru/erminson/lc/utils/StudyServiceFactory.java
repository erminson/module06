package ru.erminson.lc.utils;

import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.OrderRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.service.*;
import ru.erminson.lc.service.impl.*;

public class StudyServiceFactory {
    private StudyServiceFactory() {
        throw new IllegalStateException("StudyServiceFactory utility class");
    }

    public static StudyService createStudyService() {
        StudentRepository studentRepository = StudentRepositoryYamlInitializer.create();
        StudentService studentService = new StudentServiceImpl(studentRepository);

        CourseRepository courseRepository = CourseRepositoryYamlInitializer.create();
        CourseService courseService = new CourseServiceImpl(courseRepository);

        OrderRepository orderRepository = new OrderRepositoryImp();
        OrderService orderService = new OrderServiceImpl(orderRepository);

        RecordBookRepository recordBookRepository = RecordBookRepositoryInitializer.create(studentRepository);
        RecordBookService recordBookService = new RecordBookServiceImpl(recordBookRepository, courseService, orderService);

        return new StudyServiceImpl(studentService, recordBookService, courseService);
    }
}
