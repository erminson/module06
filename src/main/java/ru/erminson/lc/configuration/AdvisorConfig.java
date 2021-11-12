package ru.erminson.lc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "logging")
public class AdvisorConfig {
//    final LoggingPackageProperties loggingPackageProperties;
//
//    public AdvisorConfig(LoggingPackageProperties loggingPackageProperties) {
//        this.loggingPackageProperties = loggingPackageProperties;
//    }

    private static String[] pointcuts = new String[] {};

    public void setPointcuts(String[] pointcuts) {
        this.pointcuts = pointcuts;
    }

    @Bean
    public Advisor advisor() {
//        List<String> pointcuts = loggingPackageProperties.getPointcuts();
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        if (pointcuts.length == 0) {
            pointcuts = new String[]{"ru.erminson.lc"};
        }

        String pointcutExpression = Arrays.stream(pointcuts)
                .map(p -> "execution(public * ${PLACEHOLDER}..*(..))".replace("${PLACEHOLDER}", p))
                .collect(Collectors.joining(" || "));

//        if (pointcuts == null || pointcuts.isEmpty()) {
//            pointcuts = new ArrayList<>();
//            pointcuts.add("ru.erminson.lc");
//        }
//
//        String pointcutExpression = pointcuts.stream()
//                .map(p -> "execution(public * ${PLACEHOLDER}..*(..))".replace("${PLACEHOLDER}", p))
//                .collect(Collectors.joining(" || "));

        pointcut.setExpression(pointcutExpression);

        return new DefaultPointcutAdvisor(pointcut, new MethodInterceptor() {
            @Override
            public Object invoke(MethodInvocation invocation) throws Throwable {
                List<?> args = Arrays.asList(invocation.getArguments());
                log.info("Before:" + invocation.getMethod().getName() + ". Args: " + args);
                Object returning = invocation.proceed();
                log.info("After:" + invocation.getMethod().getName() + ". Return: " + returning);
                return returning;
            }
        });
    }
}
