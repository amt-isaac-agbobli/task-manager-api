package com.isaac.taskmanagementapi.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.util.jwt.interfaces.ICreateJwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Component
public class CreateJwtService implements ICreateJwtService {
    @Value("${api.security.token.secret}")
    private String secret;

    /** Creates a json web token
     *
     * @param user The authenticated user
     * @return A string containing the json web token signed with Algorithm HMAC256
     */
    public String execute(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create().withIssuer("Task-Manager-API")
                .withSubject(user.getEmail())
                .withClaim("id", user.getId())
                .withExpiresAt(getExpirationDate())
                .sign(algorithm);
    }

    /**
     * Gets an expiration date for the token based on 2 hours duration
     *
     * @return Instant object containing the expiration date
     */
    private Instant getExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}
