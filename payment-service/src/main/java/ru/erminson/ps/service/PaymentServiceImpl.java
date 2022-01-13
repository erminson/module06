package ru.erminson.ps.service;

import org.springframework.stereotype.Service;
import ru.erminson.ps.entity.PaymentEntity;
import ru.erminson.ps.repository.PaymentRepository;

import java.util.Optional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void save(PaymentEntity paymentEntity) {
        boolean result = paymentRepository.save(paymentEntity);
        if (!result) {
            throw new RuntimeException("Order not saved: " + paymentEntity.getOrderId());
        }
    }

    @Override
    public PaymentEntity findByOrderId(long id) {
        Optional<PaymentEntity> paymentEntity = paymentRepository.findByOrderId(id);
        return paymentEntity.orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }
}
