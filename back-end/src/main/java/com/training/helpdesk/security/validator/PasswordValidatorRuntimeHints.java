package com.training.helpdesk.security.validator;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;

@Component
@ImportRuntimeHints(PasswordValidatorRuntimeHints.class)
public class PasswordValidatorRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection()
                .registerType(PasswordValidator.class, MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS);
    }
}
