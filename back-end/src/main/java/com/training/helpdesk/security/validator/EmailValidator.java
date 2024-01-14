package com.training.helpdesk.security.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EmailValidator implements ConstraintValidator<EmailConstraint, String> {

    private static final String MESSAGE_IF_BLANK = "Please fill out the required field.";
    private static final String PASSWORD_PATTERN = "^[^@\\.]+(?=.*@)(?=.*\\.).*[^@\\.]+$";
    private static final Pattern PATTERN = Pattern.compile(PASSWORD_PATTERN);

    @Override
    public void initialize(EmailConstraint constraintAnnotation) {}

    @Override
    public boolean isValid(String email, ConstraintValidatorContext ctx) {
        if (email == null || email.isBlank()) {
            ctx.disableDefaultConstraintViolation();
            ctx.buildConstraintViolationWithTemplate(MESSAGE_IF_BLANK).addConstraintViolation();

            return false;
        }

        if (email.length() > 100) {
            return false;
        }

        Matcher matcher = PATTERN.matcher(email);
        return matcher.matches();

    }

}
