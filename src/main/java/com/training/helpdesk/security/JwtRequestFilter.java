package com.training.helpdesk.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.training.helpdesk.security.exception.AuthenticationTokenException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String PATH = "path";
    private static final String STATUS = "status";
    private static final String AUTH_HEADER = "Authorization";
    private static final Logger logger = LoggerFactory.getLogger(JwtRequestFilter.class);

    private Gson gson = new Gson();
    private UserDetailsService userDetailsService;
    private JwtTokenProvider jwtTokenProvider;

    public JwtRequestFilter(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response, 
            FilterChain filterChain)
            throws ServletException, IOException {
        
        String username = null;
        String token = null;

        /*
         *if (!request.getServletPath().startsWith("/rest")) {
         *    filterChain.doFilter(request, response);
         *    return;
         *}
         */

        /*Header content: Authorization: Bearer <token> */
        String requestTokenHeader = request.getHeader(AUTH_HEADER);
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(7);
            logger.info("Token: " + token);

            if (token != null) {
                try {
                    username = jwtTokenProvider.getUsernameFromToken(token);
                } catch (AuthenticationTokenException e) {
                    logger.info(e.getMessage());
                    sendError(request, response, e.getMessage());
                    return;
                }
                logger.info("Username: " + username);
            }
        } else {
            logger.error("Token doesn't start with Bearer");
        }

        if (username != null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtTokenProvider.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthToken = 
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthToken);
            }
        } else {
            logger.error("Username is null");
        }
        filterChain.doFilter(request, response);
    }

    private void sendError(HttpServletRequest request, HttpServletResponse response, String status)
            throws IOException {

        Map<String, String> body = new LinkedHashMap<>();
        body.put(PATH, "uri=" + request.getRequestURI());
        body.put(STATUS, status);
        PrintWriter out = response.getWriter();
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        out.print(gson.toJson(body));
        out.flush();
    }
}
