package ru.erminson.lc.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.soap.client.SoapFaultClientException;
import ru.erminson.lc.soap.client.PaymentClient;
import ru.erminson.paymentservice.wsdl.GetPaymentDetailsResponse;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final PaymentClient paymentClient;

    public OrderController(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @PostMapping
    public String payOrder(@RequestBody Map<String, String> body, Principal principal) {
        String username = principal.getName();
        long orderId = 1;
        BigDecimal amount = new BigDecimal(300);
        try {
        GetPaymentDetailsResponse response = paymentClient.getPaymentDetailsResponse(orderId, amount);

        } catch (SoapFaultClientException e) {
            System.out.println(e);
        }

        return "true";
    }
}
