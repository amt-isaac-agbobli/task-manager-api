package com.isaac.taskmanagementapi.config.filter;

import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.service.UserService;
import com.isaac.taskmanagementapi.util.JwtTokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.stream.Stream;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public JwtRequestFilter(UserService userService, JwtTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer ")) {
            //throw new AuthenticationException("Authorization token is null or invalid");
            System.out.println("Authorization token is null or invalid");
        }

        String token = authorizationHeader.replace("Bearer ", "").trim();

        String tokenSubject = jwtTokenUtil.getUsernameFromToken(token);

        User authenticatedUser = (User) userService.getUserByEmail(tokenSubject);

        Authentication auth = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String url = request.getRequestURI();
        return Stream.of(excluded_urls).anyMatch(url::startsWith);
    }

    private static final String[] excluded_urls = {
            "/api/auth",
            "/v3/api-docs",
            "/swagger-ui"
    };
}