package com.isaac.taskmanagementapi.repository;

import com.isaac.taskmanagementapi.entity.Task;
import com.isaac.taskmanagementapi.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    Page<User> findByEmail(String email, Pageable pageable);

    Page<User> findUserByAccountNonExpired(boolean accountNonExpired, Pageable pageable);


}
