package ru.erminson.lc.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class PaymentDto {
    @NotEmpty
    private final long orderId;

    @NotEmpty
    private final double amount;
}
