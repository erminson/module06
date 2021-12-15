package ru.erminson.lc.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.erminson.lc.mapper.OrderRowMapper;
import ru.erminson.lc.model.entity.Order;
import ru.erminson.lc.model.entity.Payment;
import ru.erminson.lc.repository.OrderRepository;
import ru.erminson.lc.utils.SqlFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class OrderRepositoryJdbc implements OrderRepository {

    private final JdbcTemplate jdbcTemplate;
    private final SqlFactory sqlFactory;

    public OrderRepositoryJdbc(JdbcTemplate jdbcTemplate, SqlFactory sqlFactory) {
        this.jdbcTemplate = jdbcTemplate;
        this.sqlFactory = sqlFactory;
    }

    @Override
    public boolean save(long studentId, long courseId, BigDecimal price, LocalDateTime createdAt) {
        String sql = sqlFactory.getSqlQuery("order/insert-order.sql");
        try {
            jdbcTemplate.update(sql, studentId, courseId, price, createdAt);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false;
        }
    }

    @Override
    public boolean save(long studentId, long courseId, BigDecimal price) {
        return save(studentId, courseId, price, LocalDateTime.now());
    }

    @Override
    public Optional<Order> findById(long id) {
        Order order = null;
        try {
            String sql = sqlFactory.getSqlQuery("order/select-by-id.sql");
            order = jdbcTemplate.queryForObject(sql, new OrderRowMapper(), id);
            log.debug("Order with id: {} found", id);
        } catch (EmptyResultDataAccessException e) {
            log.debug("Order with id: {} wasn't found", id);
        }

        return Optional.ofNullable(order);
    }

    @Override
    public boolean update(Payment payment) {
        String sql = sqlFactory.getSqlQuery("order/update-order.sql");
        try {
            jdbcTemplate.update(sql, payment.getPaymentAt(), payment.getPaymentId(), payment.getOrderId());
            log.error("Order: {} was updated", payment.getOrderId());
        } catch (DataAccessException e) {
            log.error("Order: {} wasn't updated", payment.getOrderId());
            return false;
        }

        return true;
    }

    @Override
    public List<Order> findAllByStudentId(long studentId) {
        String sql = sqlFactory.getSqlQuery("order/select-all-orders.sql");
        return jdbcTemplate.query(sql, new OrderRowMapper(), studentId);
    }
}
