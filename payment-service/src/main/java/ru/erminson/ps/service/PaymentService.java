package ru.erminson.ps.service;

import ru.erminson.ps.entity.PaymentEntity;

public interface PaymentService {
    void save(PaymentEntity paymentEntity);
    PaymentEntity findByOrderId(long id);
}
