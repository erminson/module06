package ru.erminson.lc.service;

import ru.erminson.lc.model.entity.Order;
import ru.erminson.lc.model.entity.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    void create(long studentId, long courseId, BigDecimal price, LocalDateTime createdAt);
    void create(long studentId, long courseId, BigDecimal price);
    void payForOrder(String login, Payment payment);
    List<Order> findAllByLogin(String login);
}
