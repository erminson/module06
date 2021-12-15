package ru.erminson.lc.soap.client;

import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.erminson.lc.soap.model.GetPaymentDetailsRequest;
import ru.erminson.lc.soap.model.GetPaymentDetailsResponse;

import java.math.BigDecimal;

public class PaymentClient extends WebServiceGatewaySupport {
    public GetPaymentDetailsResponse getPaymentDetailsResponse(long orderId, BigDecimal amount) {
        GetPaymentDetailsRequest request = new GetPaymentDetailsRequest();
        request.setOrderId(orderId);
        request.setAmount(amount);

        return (GetPaymentDetailsResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
