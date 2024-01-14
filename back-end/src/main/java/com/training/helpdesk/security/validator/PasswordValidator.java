package com.training.helpdesk.security.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordConstraint, String> {

    private static final String MESSAGE_IF_BLANK = "Please fill out the required field.";
    private static final String PASSWORD_PATTERN = 
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[~.\"(),:;<>@\\[\\]!#$%&'*+\\-/=?^_`{|}]).*$";
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String password, ConstraintValidatorContext ctx) {
        if (password == null || password.isBlank()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(MESSAGE_IF_BLANK).addConstraintViolation();

            return false;
        }

        if (password.length() < 6 || password.length() > 20) {
            return false;
        }

        Matcher matcher = PATTERN.matcher(password);
        return matcher.matches();

    }

}
