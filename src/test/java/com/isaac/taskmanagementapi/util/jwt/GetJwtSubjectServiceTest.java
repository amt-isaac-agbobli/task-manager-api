//package com.isaac.taskmanagementapi.util.jwt;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.test.util.ReflectionTestUtils;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//public class GetJwtSubjectServiceTest {
//
//    @InjectMocks
//    private GetJwtSubjectService getJwtSubjectService;
//
//    @Mock
//    private Algorithm algorithm;
//
//    @Mock
//    private DecodedJWT decodedJWT;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.initMocks(this);
//        ReflectionTestUtils.setField(getJwtSubjectService, "secret", "testSecret");
//    }
//
//    @Test
//    public void executeShouldReturnJwtSubject() {
//        String token = "testToken";
//        String expectedSubject = "testSubject";
//
//        when(decodedJWT.getSubject()).thenReturn(expectedSubject);
//        when(JWT.require(algorithm).withIssuer("Task-Manager-API").build().verify(token)).thenReturn(decodedJWT);
//
//        String actualSubject = getJwtSubjectService.execute(token);
//
//        assertEquals(expectedSubject, actualSubject);
//    }
//
//    @Test
//    public void executeShouldThrowExceptionWhenTokenIsInvalid() {
//        String token = "invalidToken";
//
//        when(JWT.require(algorithm).withIssuer("Task-Manager-API").build().verify(token)).thenThrow(new RuntimeException());
//
//        assertThrows(RuntimeException.class, () -> getJwtSubjectService.execute(token));
//    }
//}