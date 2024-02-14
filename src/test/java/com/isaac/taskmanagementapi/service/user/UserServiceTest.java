//package com.isaac.taskmanagementapi.service.user;
//
//import com.isaac.taskmanagementapi.entity.User;
//import com.isaac.taskmanagementapi.exception.HttpException;
//import com.isaac.taskmanagementapi.repository.UserRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class UserServiceTest {
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private UserService userService;
//
//    @BeforeEach
//    public void setup() {
//        MockitoAnnotations.openMocks(this);
//    }
//
////    @Test
////    public void getUserByEmailReturnsUserWhenEmailExists() {
////        User user = new User();
////        user.setEmail("test@test.com");
////        when(userRepository.findByEmail("test@test.com")).thenReturn(user);
////
////        User result = userService.getUserByEmail("test@test.com");
////
////        assertNotNull(result);
////        assertEquals("test@test.com", result.getEmail());
////    }
//
//    @Test
//    public void getUserByEmailReturnsNullWhenEmailDoesNotExist() {
//        when(userRepository.findByEmail("test@test.com")).thenReturn(null);
//
//        User result = userService.getUserByEmail("test@test.com");
//
//        assertNull(result);
//    }
//
////    @Test
////    public void getUserProfileReturnsUserWhenEmailExists() {
////        User user = new User();
////        user.setEmail("test@test.com");
////        when(userRepository.findByEmail("test@test.com")).thenReturn(user);
////
////        User result = userService.getUserProfile("test@test.com");
////
////        assertNotNull(result);
////        assertEquals("test@test.com", result.getEmail());
////        assertNull(result.getPassword());
////    }
//
//    @Test
//    public void getUserProfileThrowsExceptionWhenEmailDoesNotExist() {
//        when(userRepository.findByEmail("test@test.com")).thenReturn(null);
//
//        assertThrows(HttpException.class, () -> userService.getUserProfile("test@test.com"));
//    }
//}