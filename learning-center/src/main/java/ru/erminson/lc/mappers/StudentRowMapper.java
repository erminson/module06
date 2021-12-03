package ru.erminson.lc.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.erminson.lc.model.entity.Student;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentRowMapper implements RowMapper<Student> {
    public static final String ID_COLUMN = "ID";
    public static final String NAME_COLUMN = "NAME";

    @Override
    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong(ID_COLUMN));
        student.setName(rs.getString(NAME_COLUMN));

        return student;
    }
}
