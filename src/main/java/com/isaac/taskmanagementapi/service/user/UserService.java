package com.isaac.taskmanagementapi.service.user;
import com.isaac.taskmanagementapi.dto.user.UserDto;
import com.isaac.taskmanagementapi.entity.User;
import com.isaac.taskmanagementapi.exception.HttpException;
import com.isaac.taskmanagementapi.repository.UserRepository;
import com.isaac.taskmanagementapi.service.user.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


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

    public Object updateUserProfile(String email, String name) {
        User existingUser = userRepository.findByEmail(email);
        if (existingUser == null) {
            throw new HttpException("User does not exist", HttpStatus.NOT_FOUND);
        }
        existingUser.setName(name);
        userRepository.save(existingUser);
        return existingUser;
    }

    public List<UserDto> getAllUsers(Pageable pageable) {
        List<User> users = userRepository.findAll(pageable)
                .getContent();

        return users.stream().map(this::convertToDto)
                .collect(Collectors.toList());

    }

    public UserDto getFriendByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new HttpException("User does not exist", HttpStatus.NOT_FOUND);
        }
        return convertToDto(user);
    }

        private UserDto convertToDto(User user) {
            UserDto dto = new UserDto();
            dto.setId(user.getId());
            dto.setEmail(user.getEmail());
            dto.setName(user.getName());
            return dto;
        }

}
