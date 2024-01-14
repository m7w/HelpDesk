package com.training.helpdesk.security.validator;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@Component
@ImportRuntimeHints(EmailValidatorRuntimeHints.class)
public class EmailValidatorRuntimeHints implements RuntimeHintsRegistrar {
    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(EmailValidator.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
    }
}
