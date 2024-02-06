package com.isaac.taskmanagementapi.util.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GetJwtSubjectService {

    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gets the jwt subject
     *
     * @param token The json web token on request header
     * @return A string containing the subject from the decoded jwt
     */
    public String execute(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        DecodedJWT decodedJWT = JWT.require(algorithm)
                .withIssuer("Task-Manager-API")
                .build()
                .verify(token);

        return decodedJWT.getSubject();
    }
}
