package ru.erminson.ps.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentEntity {
    private long id;
    private final long orderId;
    private final BigDecimal price;
    private LocalDateTime paymentAt;

    public PaymentEntity(long orderId, BigDecimal price) {
        this.orderId = orderId;
        this.price = price;
    }
}
