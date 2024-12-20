package com.product.global.validation;

import com.product.global.validation.validator.TextColumnValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = TextColumnValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TextColumn {

    String message() default "최대 입력 사이즈를 초과하였습니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
