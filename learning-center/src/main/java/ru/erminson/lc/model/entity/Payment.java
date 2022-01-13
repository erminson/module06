package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class Payment {
    private final long orderId;
    private final BigDecimal amount;
    private LocalDateTime paymentAt;
    private long paymentId;
}
