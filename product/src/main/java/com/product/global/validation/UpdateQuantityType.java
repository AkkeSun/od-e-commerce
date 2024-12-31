package com.product.global.validation;

import com.product.global.validation.validator.UpdateQuantityTypeValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UpdateQuantityTypeValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UpdateQuantityType {

    String message() default "유효하지 않은 수정 타입 입니다";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
