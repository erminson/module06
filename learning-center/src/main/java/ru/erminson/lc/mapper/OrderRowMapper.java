package ru.erminson.lc.mapper;

import org.springframework.jdbc.core.RowMapper;
import ru.erminson.lc.model.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Objects;

public class OrderRowMapper implements RowMapper<Order> {
    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("ID"));
        order.setStudentId(rs.getLong("STUDENT_ID"));
        order.setCourseId(rs.getLong("COURSE_ID"));
        order.setPrice(rs.getBigDecimal("PRICE"));
        order.setCreatedAt(rs.getTimestamp("CREATED_AT").toLocalDateTime());

        Timestamp paymentAt = rs.getTimestamp("PAYMENT_AT");
        if (Objects.nonNull(paymentAt)) {
            order.setPaymentAt(paymentAt.toLocalDateTime());
        }

        order.setPaymentId(rs.getLong("PAYMENT_ID"));

        return order;
    }
}
