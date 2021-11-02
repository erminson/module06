package ru.erminson.lc.utils;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import ru.erminson.lc.Main;
import ru.erminson.lc.model.dto.yaml.YamlStudentList;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.repository.impl.StudentRepositoryImpl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryYamlInitializer {
    private static final String STUDENTS_FILE_NAME = "students.yaml";

    private StudentRepositoryYamlInitializer() {
        throw new IllegalStateException("StudentRepositoryYamlInitializer utility class");
    }

    public static StudentRepository create() {
        List<Student> students = loadStudentFromYamlFile();
        return new StudentRepositoryImpl(students);
    }

    private static List<Student> loadStudentFromYamlFile() {
        Yaml yaml = new Yaml(new Constructor(YamlStudentList.class));
        InputStream inputStream = Main
                .class
                .getClassLoader()
                .getResourceAsStream(STUDENTS_FILE_NAME);

        YamlStudentList list = yaml.loadAs(inputStream, YamlStudentList.class);
        return new ArrayList<>(list.getStudents());
    }
}
