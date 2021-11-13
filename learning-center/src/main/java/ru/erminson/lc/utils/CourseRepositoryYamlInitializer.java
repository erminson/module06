package ru.erminson.lc.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.lc.Main;
import ru.erminson.lc.model.dto.yaml.YamlCourseList;
import ru.erminson.lc.model.entity.Course;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.impl.CourseRepositoryImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CourseRepositoryYamlInitializer {
    private static final String COURSES_FILE_NAME = "courses.yaml";

    private CourseRepositoryYamlInitializer() {
        throw new IllegalStateException("CourseRepositoryYamlInitializer utility class");
    }

    public static CourseRepository create() {
        List<Course> courses = loadCoursesFromYamlFile();
        return new CourseRepositoryImpl(courses);
    }

    private static List<Course> loadCoursesFromYamlFile() {
        Yaml yaml = new Yaml(new Constructor(YamlCourseList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(COURSES_FILE_NAME);

        YamlCourseList list = yaml.loadAs(inputStream, YamlCourseList.class);
        return new ArrayList<>(list.getCourses());
    }
}
