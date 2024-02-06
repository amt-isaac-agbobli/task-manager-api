package com.isaac.taskmanagementapi.config.filter;

import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.service.UserService;
import com.isaac.taskmanagementapi.util.GetJwtSubjectService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    private final GetJwtSubjectService getJwtSubjectService;

    @Autowired
    public JwtRequestFilter(UserService userService, GetJwtSubjectService getJwtSubjectService) {
        this.userService = userService;
        this.getJwtSubjectService = getJwtSubjectService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer ")) {
            throw new HttpException("HTTP header is required", HttpStatus.UNAUTHORIZED);
        }

        String token = authorizationHeader.replace("Bearer ", "").trim();

        String tokenSubject = this.getJwtSubjectService.execute(token);

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
            "/api/v1/auth",
            "/v3/api-docs",
            "/swagger-ui"
    };
}