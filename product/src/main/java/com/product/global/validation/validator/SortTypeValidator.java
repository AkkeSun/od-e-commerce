package com.product.global.validation.validator;

import com.product.global.validation.SortType;
import com.product.product.domain.ProductSortType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SortTypeValidator implements ConstraintValidator<SortType, String> {

    @Override
    public boolean isValid(String input, ConstraintValidatorContext context) {
        try {
            ProductSortType.valueOf(input);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
