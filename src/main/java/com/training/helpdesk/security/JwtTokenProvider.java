package com.training.helpdesk.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.training.helpdesk.security.exception.AuthenticationTokenException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenProvider {

    private static final Long VALIDITY = 5L * 60 * 60 * 1000;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getAllClaimsFromToken(token).getExpiration();
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            throw new AuthenticationTokenException("JWT token expired");
        } catch (JwtException | IllegalArgumentException e) {
            throw new AuthenticationTokenException("Error parsing JWT token");
        }
    }

    private boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        long now = System.currentTimeMillis();
        String subject = userDetails.getUsername();

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(now))
                .setExpiration(new Date(now + VALIDITY)).signWith(SignatureAlgorithm.HS256, secret).compact();
    }
}
