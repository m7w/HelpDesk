package com.training.helpdesk.security.controller;

import javax.validation.Valid;

import com.training.helpdesk.security.dto.UserCredentialsDto;
import com.training.helpdesk.security.util.JwtUtils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public JwtAuthenticationController(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@Valid @RequestBody UserCredentialsDto authRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        String token = jwtUtils.createToken(authentication);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("Authorization", token);

        return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
    }
}
