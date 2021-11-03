package ru.erminson.lc.bean_post_processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;
import ru.erminson.lc.annotation.InjectRandomMark;

import java.lang.reflect.Method;
import java.util.Random;

@Component
public class InjectRandomMarkAnnotationBeanPostProcessor implements BeanPostProcessor {
    private final Random random = new Random();

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            InjectRandomMark annotation = method.getAnnotation(InjectRandomMark.class);
            if (annotation != null) {
                int min = annotation.min();
                int max = annotation.max();
                int mark = random.nextInt(max - min) + min;
                ReflectionUtils.invokeMethod(method, bean, mark);
                break;
            }
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
