package ru.erminson.ps.repository;

import ru.erminson.ps.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentRepository {
    boolean save(PaymentEntity paymentEntity);
    Optional<PaymentEntity> findByOrderId(long orderId);
}
