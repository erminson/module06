package ru.erminson.lc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class AdvisorConfig {
    private static final String DEFAULT_PACKAGE = "ru.erminson.lc";

    private final LoggingPackageProperties loggingPackageProperties;

    public AdvisorConfig(LoggingPackageProperties loggingPackageProperties) {
        this.loggingPackageProperties = loggingPackageProperties;
    }

    @Bean
    public Advisor advisor() {
        List<String> packages = loggingPackageProperties.getPackages();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        if (packages == null || packages.isEmpty()) {
            packages = new ArrayList<>();
            packages.add(DEFAULT_PACKAGE);
        }

        String pointcutExpression = packages.stream()
                .map(p -> "execution(public * ${PLACEHOLDER}..*(..))".replace("${PLACEHOLDER}", p))
                .collect(Collectors.joining(" || "));

        pointcut.setExpression(pointcutExpression);

        return new DefaultPointcutAdvisor(pointcut, (MethodInterceptor) invocation -> {
            List<?> args = Arrays.asList(invocation.getArguments());
            log.info("Before:" + invocation.getMethod().getName() + ". Args: " + args);
            Object returning = invocation.proceed();
            log.info("After:" + invocation.getMethod().getName() + ". Return: " + returning);
            return returning;
        });
    }
}
