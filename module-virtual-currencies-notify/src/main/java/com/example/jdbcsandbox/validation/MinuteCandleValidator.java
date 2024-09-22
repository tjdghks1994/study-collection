package com.example.jdbcsandbox.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.ArrayList;
import java.util.List;

@Component
public class MinuteCandleValidator implements Validator {
    private List<Integer> allowedUnits = new ArrayList<>();

    public MinuteCandleValidator() {
        // 허용되는 unit 값 초기 세팅
        allowedUnits.add(1);
        allowedUnits.add(3);
        allowedUnits.add(5);
        allowedUnits.add(10);
        allowedUnits.add(15);
        allowedUnits.add(30);
        allowedUnits.add(60);
        allowedUnits.add(240);
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(Integer.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Integer unit = (Integer) target;

        if (!allowedUnits.contains(unit)) {
            errors.rejectValue("unit", "invalid unit");
        }
    }
}
