package ru.erminson.lc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.erminson.lc.mapper.PaymentMapper;
import ru.erminson.lc.model.dto.PaymentDto;
import ru.erminson.lc.model.entity.Order;
import ru.erminson.lc.model.entity.Payment;
import ru.erminson.lc.service.OrderService;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> payOrder(@RequestBody PaymentDto paymentDto, Principal principal) {
        String login = principal.getName();
        Payment payment = PaymentMapper.payment(paymentDto);

        orderService.payForOrder(login, payment);

        return ResponseEntity.ok("Order successfully paid ");
    }

    @GetMapping
    public List<Order> orders(Principal principal) {
        String login = principal.getName();
        return orderService.findAllByLogin(login);
    }
}
