package ru.erminson.lc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.soap.client.SoapFaultClientException;
import ru.erminson.lc.model.entity.Order;
import ru.erminson.lc.model.entity.Payment;
import ru.erminson.lc.model.entity.User;
import ru.erminson.lc.model.exception.EntityNotFoundException;
import ru.erminson.lc.repository.OrderRepository;
import ru.erminson.lc.service.OrderService;
import ru.erminson.lc.service.UserService;
import ru.erminson.lc.soap.client.PaymentClient;
import ru.erminson.lc.soap.model.GetPaymentDetailsResponse;
import ru.erminson.lc.soap.model.ObjectFactory;
import ru.erminson.lc.soap.model.PaymentDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private PaymentClient paymentClient;
    private UserService userService;

    private ObjectFactory objectFactory;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setPaymentClient(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Autowired
    public void setUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    @Override
    public void create(long studentId, long courseId, BigDecimal price, LocalDateTime createdAt) {
        boolean result = orderRepository.save(studentId, courseId, price, createdAt);
        if (!result) {
            // TODO: create special Exception
            throw new RuntimeException();
        }
    }

    @Override
    public void create(long studentId, long courseId, BigDecimal price) {
        create(studentId, courseId, price, LocalDateTime.now());
    }

    @Override
    public void payForOrder(String login, Payment payment) {
        Optional<Order> orderOptional = orderRepository.findById(payment.getOrderId());
        Order order = orderOptional.orElseThrow(
                () -> new EntityNotFoundException(Order.class, "orderId", String.valueOf(payment.getOrderId())));
        User user = userService.findByLogin(login);

        if (user.getId() != order.getStudentId()) {
            throw new EntityNotFoundException(
                    Order.class,
                    "orderId", String.valueOf(payment.getOrderId()),
                    "login", user.getLogin()
            );
        }

        if (payment.getAmount().equals(order.getPrice())) {
            // TODO: create special Exception
            throw new RuntimeException();
        }

        try {
            GetPaymentDetailsResponse response = paymentClient.getPaymentDetails(
                    payment.getOrderId(), payment.getAmount()
            );
            DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
            PaymentDetails paymentDetails = response.getPaymentDetails();
            Payment updatePayment = Payment.builder()
                    .orderId(paymentDetails.getOrderId())
                    .amount(paymentDetails.getAmount())
                    .paymentAt(LocalDateTime.parse(paymentDetails.getPaymentAt(), formatter))
                    .paymentId(paymentDetails.getId())
                    .build();
            orderRepository.update(updatePayment);
        } catch (SoapFaultClientException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> findAllByLogin(String login) {
        User student = userService.findByLogin(login);

        return orderRepository.findAllByStudentId(student.getId());
    }
}
