package com.training.helpdesk.mail.service.impl;

import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.context.annotation.ImportRuntimeHints;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.EngineContext;

@Component
@ImportRuntimeHints(EngineContextRuntimeHints.class)
public class EngineContextRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.reflection().registerType(EngineContext.class, MemberCategory.INVOKE_PUBLIC_METHODS);
    }
    
}
