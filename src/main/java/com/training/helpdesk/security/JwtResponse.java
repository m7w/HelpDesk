package com.training.helpdesk.security;

import java.io.Serializable;

public class JwtResponse implements Serializable {

    private static final long serialVersionUID = -7918116408939728756L;

    private String jwtToken;

    public JwtResponse(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
