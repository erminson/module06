package ru.erminson.lc.utils;

import ru.erminson.lc.model.entity.Order;
import ru.erminson.lc.model.entity.Payment;
import ru.erminson.lc.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class OrderRepositoryImp implements OrderRepository {
    @Override
    public boolean save(long studentId, long courseId, BigDecimal price, LocalDateTime createdAt) {
        return false;
    }

    @Override
    public boolean save(long studentId, long courseId, BigDecimal price) {
        return false;
    }

    @Override
    public Optional<Order> findById(long id) {
        return Optional.empty();
    }

    @Override
    public boolean update(Payment payment) {
        return false;
    }

    @Override
    public List<Order> findAllByStudentId(long studentId){
        return Collections.emptyList();
    }
}
