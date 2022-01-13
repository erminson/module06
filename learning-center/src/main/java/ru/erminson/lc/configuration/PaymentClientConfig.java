package ru.erminson.lc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import ru.erminson.lc.soap.client.PaymentClient;

@Configuration
@Slf4j
public class PaymentClientConfig {
    @Value("${soap.payment-url}")
    private String paymentURL;

    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ru.erminson.lc.soap.model");

        return marshaller;
    }

    @Bean
    public PaymentClient paymentClient(Jaxb2Marshaller marshaller) {
        log.info("Soap service URL: {}", paymentURL);
        PaymentClient paymentClient = new PaymentClient();
        paymentClient.setDefaultUri(paymentURL);
        paymentClient.setMarshaller(marshaller);
        paymentClient.setUnmarshaller(marshaller);

        return paymentClient;
    }
}
