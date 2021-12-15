package ru.erminson.lc.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private long id;
    private long studentId;
    private long courseId;
    private BigDecimal price;
    private LocalDateTime createdAt;
    private LocalDateTime paymentAt;
    private long paymentId;

    public Order(long id, long studentId, long courseId, BigDecimal price, LocalDateTime createdAt) {
        this.id = id;
        this.studentId = studentId;
        this.courseId = courseId;
        this.price = price;
        this.createdAt = createdAt;
    }
}
