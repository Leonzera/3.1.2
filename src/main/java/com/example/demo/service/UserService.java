package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> listUsers();
    User getUserById(Long id);
    void saveOrUpdateUser(User user);
    void deleteUserById(Long id);
    User getUserByUsername(String username);
}