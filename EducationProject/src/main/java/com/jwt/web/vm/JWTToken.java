package com.jwt.web.vm;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jwt.security.jwt.JWTConfigurer;

/**
 * Object to return as body in JWT Authentication.
 */
public class JWTToken {

    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @JsonProperty(JWTConfigurer.AUTHORIZATION_HEADER)
    public String getToken() {
        return token;
    }

}
