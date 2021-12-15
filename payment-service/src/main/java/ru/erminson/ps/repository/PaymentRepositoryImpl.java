package ru.erminson.ps.repository;

import org.springframework.stereotype.Repository;
import ru.erminson.ps.entity.PaymentEntity;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class PaymentRepositoryImpl implements PaymentRepository {
    private int lastId = 3;
    public final Map<Long, PaymentEntity> storage;

    public PaymentRepositoryImpl() {
        this.storage = new HashMap<>();
    }

    @Override
    public boolean save(PaymentEntity paymentEntity) {
        if (!containsOrderId(paymentEntity.getOrderId())) {
            paymentEntity.setId(++lastId);
            paymentEntity.setPaymentAt(LocalDateTime.now());
            storage.put(paymentEntity.getId(), paymentEntity);

            return true;
        }

        return false;
    }

    @Override
    public Optional<PaymentEntity> findByOrderId(long orderId) {
        return storage.values().stream()
                .filter(paymentEntity -> paymentEntity.getOrderId() == orderId).findFirst();
    }

    private boolean containsOrderId(long orderId) {
        return storage.values().stream()
                .anyMatch(paymentEntity -> paymentEntity.getOrderId() == orderId);
    }
}
