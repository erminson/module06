package ru.erminson.ps.endpoint;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import ru.erminson.ps.entity.PaymentEntity;
import ru.erminson.ps.service.PaymentService;
import ru.erminson.ps.soap.payment.GetPaymentDetailsRequest;
import ru.erminson.ps.soap.payment.GetPaymentDetailsResponse;
import ru.erminson.ps.soap.payment.PaymentDetails;

import java.time.format.DateTimeFormatter;

@Endpoint
public class PaymentEndpoint {
    private static final String NAMESPACE_URI = "http://www.erminson.ru/ps/soap/payment";

    private final PaymentService paymentService;

    public PaymentEndpoint(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetPaymentDetailsRequest")
    @ResponsePayload
    public GetPaymentDetailsResponse getPaymentDetails(@RequestPayload GetPaymentDetailsRequest request) {
        paymentService.save(mapRequestToEntity(request));
        PaymentEntity paymentEntity = paymentService.findByOrderId(request.getOrderId());
        return mapEntityToResponse(paymentEntity);
    }

    private PaymentEntity mapRequestToEntity(GetPaymentDetailsRequest request) {
        return new PaymentEntity(request.getOrderId(), request.getAmount());
    }

    private GetPaymentDetailsResponse mapEntityToResponse(PaymentEntity paymentEntity) {
        GetPaymentDetailsResponse response = new GetPaymentDetailsResponse();

        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setId(paymentEntity.getId());
        paymentDetails.setOrderId(paymentEntity.getOrderId());
        paymentDetails.setAmount(paymentEntity.getPrice());
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        paymentDetails.setPaymentAt(paymentEntity.getPaymentAt().format(formatter));

        response.setPaymentDetails(paymentDetails);

        return response;
    }
}
