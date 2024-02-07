package com.isaac.taskmanagementapi.service.user;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.service.user.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;


    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    @Cacheable(value = "userProfile", key = "#email")
    public User getUserProfile(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new HttpException("User does not exist", HttpStatus.NOT_FOUND);
        }
        user.setPassword(null);
        return user;
    }

}
