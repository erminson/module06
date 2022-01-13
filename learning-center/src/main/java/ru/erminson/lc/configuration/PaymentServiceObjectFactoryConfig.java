package ru.erminson.lc.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.lc.soap.model.ObjectFactory;

@Configuration
public class PaymentServiceObjectFactoryConfig {
    @Bean
    public ObjectFactory createObjectFactory() {
        return new ObjectFactory();
    }
}
