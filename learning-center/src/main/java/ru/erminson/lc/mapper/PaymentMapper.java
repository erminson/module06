package ru.erminson.lc.mapper;

import ru.erminson.lc.model.dto.PaymentDto;
import ru.erminson.lc.model.entity.Payment;

import java.math.BigDecimal;

public final class PaymentMapper {
    private PaymentMapper() {}

    public static Payment payment(PaymentDto paymentDto) {
        return Payment.builder()
                .orderId(paymentDto.getOrderId())
                .amount(BigDecimal.valueOf(paymentDto.getAmount()))
                .build();
    }
}
