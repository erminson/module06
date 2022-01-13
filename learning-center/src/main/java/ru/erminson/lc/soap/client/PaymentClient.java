package ru.erminson.lc.soap.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import ru.erminson.lc.soap.model.GetPaymentDetailsRequest;
import ru.erminson.lc.soap.model.GetPaymentDetailsResponse;
import ru.erminson.lc.soap.model.ObjectFactory;

import java.math.BigDecimal;

public class PaymentClient extends WebServiceGatewaySupport {
    private ObjectFactory objectFactory;

    @Autowired
    public void setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
    }

    public GetPaymentDetailsResponse getPaymentDetails(long orderId, BigDecimal amount) {
        GetPaymentDetailsRequest request =  objectFactory.createGetPaymentDetailsRequest();
        request.setOrderId(orderId);
        request.setAmount(amount);

        return (GetPaymentDetailsResponse) getWebServiceTemplate().marshalSendAndReceive(request);
    }
}
