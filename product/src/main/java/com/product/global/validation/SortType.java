package com.product.global.validation;

import com.product.global.validation.validator.SortTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = SortTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortType {

    String message() default "유효하지 않은 정렬 타입 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
