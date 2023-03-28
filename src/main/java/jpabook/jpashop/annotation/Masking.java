package jpabook.jpashop.annotation;

import jpabook.jpashop.constant.MaskingType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Masking {

    boolean mask() default true;

    MaskingType type();
}
