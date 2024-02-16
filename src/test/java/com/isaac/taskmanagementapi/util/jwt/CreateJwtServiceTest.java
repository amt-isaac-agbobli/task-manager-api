package com.isaac.taskmanagementapi.util.jwt;

import com.auth0.jwt.JWT;
import com.isaac.taskmanagementapi.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


class CreateJwtServiceTest {

    @InjectMocks
    private CreateJwtService createJwtService;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(createJwtService, "secret", "testSecret");
    }

    @Test
    void executeShouldReturnJwtToken() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setId(1);

        String token = createJwtService.execute(user);

        assertNotNull(token);
        assertEquals("test@test.com", JWT.decode(token).getSubject());
        assertEquals(1, JWT.decode(token).getClaim("id").asLong());
    }

    @Test
    void executeShouldReturnJwtTokenWithExpirationDate() {
        User user = new User();
        user.setEmail("test@test.com");
        user.setId(1);

        String token = createJwtService.execute(user);

        assertNotNull(token);
        Instant expirationDate = JWT.decode(token).getExpiresAt().toInstant();
        assertTrue(expirationDate.isAfter(Instant.now()));
    }
}