package com.example.bean.impl;

import com.example.bean.exception.UnauthorizedException;
import com.example.common.util.JwtTokenProvider;
import com.example.service.interfaces.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.util.StringUtils.hasText;

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private JwtTokenProvider tokenProvider = JwtTokenProvider.getInstance();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader("Authorization");

        if (!hasText(token)) {
            throw new UnauthorizedException("Not found authorization token");
        }
        if (!tokenProvider.isValid(token)) {
            throw new UnauthorizedException("Authorization token is not valid");
        }

        filterChain.doFilter(request, response);
    }
}