package com.training.helpdesk.security.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import jakarta.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PasswordValidatorTest {
    
    private static final String VALID_PASSWORD = "P@ssword1";
    private static final String INVALID_PASSWORD = "Password1";

    private final PasswordValidator passwordValidator = new PasswordValidator();

    @Mock
    private ConstraintValidatorContext ctx;

    @Test
    public void testIsValid_Valid() {
        Boolean actual = passwordValidator.isValid(VALID_PASSWORD, ctx);

        assertTrue(actual);
    }

    @Test
    public void testIsValid_InValid() {
        Boolean actual = passwordValidator.isValid(INVALID_PASSWORD, ctx);

        assertFalse(actual);
    }
}

