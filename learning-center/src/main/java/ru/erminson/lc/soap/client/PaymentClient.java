package ru.erminson.lc.soap.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.erminson.paymentservice.wsdl.GetPaymentDetailsRequest;
import ru.erminson.paymentservice.wsdl.GetPaymentDetailsResponse;

import java.math.BigDecimal;

public class PaymentClient extends WebServiceGatewaySupport {
    public GetPaymentDetailsResponse getPaymentDetailsResponse(long orderId, BigDecimal amount) {
        GetPaymentDetailsRequest request = new GetPaymentDetailsRequest();
        request.setOrderId(orderId);
        request.setAmount(amount);

        return (GetPaymentDetailsResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
