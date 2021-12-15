package ru.erminson.lc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import ru.erminson.lc.soap.client.PaymentClient;

@Configuration
public class PaymentClientConfig {
    @Bean
    public Jaxb2Marshaller marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("ru.erminson.lc.soap.model");

        return marshaller;
    }

    @Bean
    public PaymentClient paymentClient(Jaxb2Marshaller marshaller) {
        PaymentClient paymentClient = new PaymentClient();
        paymentClient.setDefaultUri("http://localhost:8082/ws");
        paymentClient.setMarshaller(marshaller);
        paymentClient.setUnmarshaller(marshaller);

        return paymentClient;
    }
}
