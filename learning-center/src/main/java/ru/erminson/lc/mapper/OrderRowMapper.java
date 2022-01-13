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
        order.setId(rs.getLong("id"));
        order.setStudentId(rs.getLong("student_id"));
        order.setCourseId(rs.getLong("course_id"));
        order.setPrice(rs.getBigDecimal("price"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

        Timestamp paymentAt = rs.getTimestamp("payment_at");
        if (Objects.nonNull(paymentAt)) {
            order.setPaymentAt(paymentAt.toLocalDateTime());
        }

        order.setPaymentId(rs.getLong("payment_id"));

        return order;
    }
}
