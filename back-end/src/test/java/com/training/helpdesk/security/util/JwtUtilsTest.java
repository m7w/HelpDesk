package com.training.helpdesk.security.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import com.training.helpdesk.security.SecurityUser;
import com.training.helpdesk.user.domain.Role;
import com.training.helpdesk.user.domain.User;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class JwtUtilsTest {

    private static final User EMPLOYEE = new User(1L, "Lenny", "Leonart", Role.ROLE_EMPLOYEE, 
            "user1_mogilev@yopmail.com", "password");
    private static final String SECRET = "LongSecretSecretSecretSecretSecret";
    private static final long VALIDITY = 10000;

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Test
    public void testCreateToken_ValidateToken_ParseToken() {
        when(authentication.getPrincipal()).thenReturn(new SecurityUser(EMPLOYEE));

        ReflectionTestUtils.setField(jwtUtils, "secret", SECRET);
        ReflectionTestUtils.setField(jwtUtils, "validity", VALIDITY);

        String token = jwtUtils.createToken(authentication);

        assertTrue(jwtUtils.validateToken(token));

        assertEquals(EMPLOYEE.getId(), jwtUtils.getId(token));
        assertEquals(EMPLOYEE.getEmail(), jwtUtils.getUsername(token));
        assertEquals(EMPLOYEE.getRole().toString(), jwtUtils.getAuthorities(token));
    }
}
