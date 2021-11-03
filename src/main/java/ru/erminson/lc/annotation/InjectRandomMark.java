package ru.erminson.lc.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InjectRandomMark {
    int min() default 1;
    int max() default 100;
}
