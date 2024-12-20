package com.product.global.validation.validator;

import com.product.global.validation.TextColumn;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.nio.charset.StandardCharsets;

public class TextColumnValidator implements ConstraintValidator<TextColumn, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        if (input == null) {
            return true;
        }
        return input.getBytes(StandardCharsets.UTF_8).length <= 65535;
    }
}
