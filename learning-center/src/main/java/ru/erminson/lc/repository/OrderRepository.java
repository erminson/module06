package ru.erminson.lc.repository;

import ru.erminson.lc.model.entity.Order;
import ru.erminson.lc.model.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {
    boolean save(long studentId, long courseId, BigDecimal price, LocalDateTime createdAt);
    boolean save(long studentId, long courseId, BigDecimal price);
    Optional<Order> findById(long id);
    boolean update(Payment payment);
    List<Order> findAllByStudentId(long studentId);
}
