package ru.erminson.lc.utils;

import ru.erminson.lc.model.entity.RecordBook;
import ru.erminson.lc.model.entity.Student;
import ru.erminson.lc.service.RecordBookService;

import java.time.LocalDate;
import java.util.Comparator;

public class StudentComparatorFactory {
    private StudentComparatorFactory() {
        throw new IllegalStateException("StudentComparatorFactory utility class");
    }

    public static Comparator<Student> getAverageComparator(RecordBookService recordBookService) {
        return (o1, o2) -> {
            RecordBook rb1 = recordBookService.getRecordBookByStudent(o1);
            RecordBook rb2 = recordBookService.getRecordBookByStudent(o2);

            return Double.compare(rb1.getAverageScore(), rb2.getAverageScore());
        };
    }

    public static Comparator<Student> getDaysUntilEndOfCourseComparator(RecordBookService recordBookService, LocalDate nowDate) {
        return (o1, o2) -> {
            int days1 = recordBookService.getDaysUntilEndOfCourseByStudent(o1, nowDate);
            int days2 = recordBookService.getDaysUntilEndOfCourseByStudent(o2, nowDate);

            return Integer.compare(days1, days2);
        };
    }

    public static Comparator<Student> getStudentNameComparator() {
        return Comparator.comparing(Student::getName);
    }

    public static Comparator<Student> getStartDateComparator(RecordBookService recordBookService) {
        return (o1, o2) -> {
            LocalDate startDate1 = recordBookService.getRecordBookByStudent(o1).getStartDate();
            LocalDate startDate2 = recordBookService.getRecordBookByStudent(o2).getStartDate();

            return startDate1.compareTo(startDate2);
        };
    }

    public static Comparator<Student> getCourseComparator(RecordBookService recordBookService) {
        return (o1, o2) -> {
            String courseTitle1 = recordBookService.getRecordBookByStudent(o1).getCourseTitle();
            String courseTitle2 = recordBookService.getRecordBookByStudent(o2).getCourseTitle();

            return courseTitle1.compareTo(courseTitle2);
        };
    }
}
