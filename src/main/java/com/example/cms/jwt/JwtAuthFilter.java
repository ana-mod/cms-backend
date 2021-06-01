package com.example.cms.jwt;


import com.example.cms.services.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired JwtProvider provider;

    @Autowired
    AuthenticationService service;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final String HEADER_PREFIX = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            String token = getToken(httpServletRequest);
            if(token != null && provider.isTokenValid(token)) {
                String username = provider.getUsernameFromToken(token);
                UserDetails userDetails = service.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        } catch (Exception e) {
            logger.error("User can not be authorized -> Message: {}", e.getMessage());
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith(HEADER_PREFIX)) {
            return authHeader.replace(HEADER_PREFIX, "");
        }

        return null;
    }

}
