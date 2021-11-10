package ru.erminson.lc.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import ru.erminson.lc.repository.CourseRepository;
import ru.erminson.lc.repository.RecordBookRepository;
import ru.erminson.lc.repository.StudentRepository;
import ru.erminson.lc.utils.CourseRepositoryYamlInitializer;
import ru.erminson.lc.utils.RecordBookRepositoryInitializer;
import ru.erminson.lc.utils.StudentRepositoryYamlInitializer;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@ComponentScan("ru.erminson.lc")
@EnableAspectJAutoProxy
@Import(TopicScoreConfig.class)
public class AppConfig {
    @Bean
    public CourseRepository createCourseRepository() {
        return CourseRepositoryYamlInitializer.create();
    }

    @Bean
    public StudentRepository createStudentRepository() {
        return StudentRepositoryYamlInitializer.create();
    }

    @Bean
    @Autowired
    public RecordBookRepository createRecordBookRepository(StudentRepository studentRepository) {
        return RecordBookRepositoryInitializer.create(studentRepository);
    }

    @Value("${pointcut.property}")
    private String pointcutExpression;

    @Value("${pointcut.properties}")
    private String[] pointcutExpressions;

    @Bean
    public Advisor advisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
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
