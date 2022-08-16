package com.training.helpdesk.security.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.helpdesk.security.dto.UserCredentialsDto;
import com.training.helpdesk.security.util.JwtUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationControllerTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private static final String TOKEN="Some token";

    private MockMvc mockMvc;

    @Mock 
    private AuthenticationManager authenticationManager;

    @Mock 
    private Authentication authentication;

    @Mock
    private JwtUtils jwtUtils;

    @Test
    public void testCreateToken() throws Exception {
        final JwtAuthenticationController controller = new JwtAuthenticationController(authenticationManager, jwtUtils);
        final UserCredentialsDto credentials = new UserCredentialsDto("user1_mogilev@yopmail.com", "P@ssword1");
        final ObjectMapper mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtils.createToken(authentication)).thenReturn(TOKEN);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsBytes(credentials)))
            .andExpect(status().isOk())
            .andExpect(header().stringValues(AUTHORIZATION_HEADER, TOKEN));
    }
}
