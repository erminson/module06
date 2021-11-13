package ru.erminson.logging.configuration;

import org.aopalliance.intercept.MethodInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.erminson.logging.annotation.EnableCustomAOP;
import ru.erminson.logging.model.LoggingPackageProperties;
import ru.erminson.logging.exception.IllegalPackagesException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@ConditionalOnProperty(value = "logging.enabled", havingValue = "true")
@EnableConfigurationProperties(LoggingPackageProperties.class)
public class LoggerAutoConfiguration {
    private static final String ANNOTATION_POINTCUT  = "@annotation(" + EnableCustomAOP.class.getName() + ")";

    private final Logger log = LoggerFactory.getLogger(LoggerAutoConfiguration.class);

    private final LoggingPackageProperties loggingPackageProperties;

    public LoggerAutoConfiguration(LoggingPackageProperties loggingPackageProperties) {
        this.loggingPackageProperties = loggingPackageProperties;
    }

    @Bean
    public Advisor advisor() throws IllegalPackagesException {
        List<String> packages = loggingPackageProperties.getPackages();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        if (packages == null || packages.isEmpty()) {
            throw new IllegalPackagesException("Packages are missing from application properties file");
        }

        String pointcutExpression = packages.stream()
                .map(p -> "execution(public * ${PLACEHOLDER}..*(..))".replace("${PLACEHOLDER}", p))
                .collect(Collectors.joining(" || "));

        pointcutExpression += " || " + ANNOTATION_POINTCUT;

        pointcut.setExpression(pointcutExpression);

        return new DefaultPointcutAdvisor(pointcut, (MethodInterceptor) invocation -> {
            List<?> args = Arrays.asList(invocation.getArguments());
            String methodName = invocation.getMethod().getName();
            log.info("Before: {}. Args: {}", methodName, args);
            Object returning = invocation.proceed();
            log.info("After: {}. Return: {}", methodName, returning);
            return returning;
        });
    }
}
