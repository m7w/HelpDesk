package com.training.helpdesk.security.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.validation.ConstraintValidatorContext;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {
    
    private static final String VALID_EMAIL = "manager1_mogilev@yopmail.com";
    private static final String INVALID_EMAIL = "@user@gmail.com";

    private final EmailValidator emailValidator = new EmailValidator();

    @Mock
    private ConstraintValidatorContext ctx;

    @Test
    public void testIsValid_Valid() {
        Boolean actual = emailValidator.isValid(VALID_EMAIL, ctx);

        assertTrue(actual);
    }

    @Test
    public void testIsValid_InValid() {
        Boolean actual = emailValidator.isValid(INVALID_EMAIL, ctx);

        assertFalse(actual);
    }
}
