package ru.erminson.lc.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.List;

//@Component
//@Aspect
@Slf4j
public class LoggingAspect {
    @Pointcut("within(ru.erminson.lc..*)")
    private void allMethods() {}

    @Pointcut("execution(public Object postProcessBeforeInitialization(..))")
    private void postProcessBeforeInitializationAdvice() {}

    @Pointcut("execution(public Object postProcessAfterInitialization(..))")
    private void postProcessAfterInitializationAdvice() {}

    @Pointcut("!postProcessBeforeInitializationAdvice() && !postProcessAfterInitializationAdvice()")
    private void allMethodExcludeBPP() {}


    @Before("allMethodExcludeBPP()")
    public void beforeAllMethodsLoggingAdvice(JoinPoint joinPoint) {
        List<?> args = Arrays.asList(joinPoint.getArgs());
        log.info("Before:" + joinPoint.getSignature().toShortString() + ". Args: " + args);
    }

    @AfterReturning(pointcut = "allMethodExcludeBPP()", returning = "returning")
    public void afterAllMethodsLoggingAdvice(JoinPoint joinPoint, Object returning) {
        log.info("After:" + joinPoint.getSignature().toShortString() + ". Return: " + returning);
    }
}
