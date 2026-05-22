package com.example.demo.service;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return user;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> listUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    @Transactional
    public void saveOrUpdateUser(User user) {
        if (user.getId() == null) {
            if (user.getPassword() != null) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getRoles().isEmpty()) {
                Role defaultRole = roleService.getRoleByName("ROLE_USER");
                if (defaultRole != null) {
                    user.getRoles().add(defaultRole);
                }
            }
        } else {
            User existingUser = getUserById(user.getId());
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                user.setPassword(existingUser.getPassword());
            } else if (!user.getPassword().equals(existingUser.getPassword())) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getRoles().isEmpty()) {
                user.setRoles(existingUser.getRoles());
            }
        }
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}