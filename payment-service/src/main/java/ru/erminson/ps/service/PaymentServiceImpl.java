package ru.erminson.ps.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.erminson.ps.entity.PaymentEntity;
import ru.erminson.ps.repository.PaymentRepository;

import java.util.Optional;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public void save(PaymentEntity paymentEntity) {
        log.info("Save: {}", paymentEntity);
        boolean result = paymentRepository.save(paymentEntity);
        if (!result) {
            String message = "Order not saved: " + paymentEntity.getOrderId();
            log.info("Save. Error: {}", message);
            throw new RuntimeException(message);
        }
    }

    @Override
    public PaymentEntity findByOrderId(long id) {
        log.info("Find by id: {}", id);
        Optional<PaymentEntity> paymentEntity = paymentRepository.findByOrderId(id);
        return paymentEntity.orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }
}
