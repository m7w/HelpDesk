package com.training.helpdesk.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.training.helpdesk.user.domain.User;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtRequestFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtRequestFilter.class);
    private static final String BEARER = "Bearer";
    private static final String AUTH_HEADER = "Authorization";

    private final JwtUtils jwtUtils;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            String token = authHeader.substring(7);
            try{ 
                jwtUtils.validateToken(token);
                String username = jwtUtils.getUsername(token);
                Long id = jwtUtils.getId(token);
                User user = new User();
                user.setEmail(username);
                user.setId(id);
                String authorities = jwtUtils.getAuthorities(token);
                Authentication auth = new UsernamePasswordAuthenticationToken(new SecurityUser(user), null,
                        AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (Exception e) {
                LOGGER.error("Cannot set user authentication: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
