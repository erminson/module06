package ru.erminson.ps.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentEntity {
    private long id;
    private long orderId;
    private BigDecimal price;
    private LocalDateTime paymentAt;

    public PaymentEntity(long orderId, BigDecimal price) {
        this.orderId = orderId;
        this.price = price;
    }
}
