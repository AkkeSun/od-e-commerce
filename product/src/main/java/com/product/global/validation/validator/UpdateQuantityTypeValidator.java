package com.product.global.validation.validator;

import com.product.global.validation.UpdateQuantityType;
import com.product.product.domain.QuantityType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UpdateQuantityTypeValidator implements
    ConstraintValidator<UpdateQuantityType, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        try {
            QuantityType.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
