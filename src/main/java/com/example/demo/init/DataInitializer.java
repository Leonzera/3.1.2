package com.example.demo.init;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserService userService;
    private final RoleService roleService;

    @Override
    public void run(String... args) {
        Role adminRole = roleService.getRoleByName("ROLE_ADMIN");
        if (adminRole == null) {
            adminRole = new Role("ROLE_ADMIN");
            roleService.saveRole(adminRole);
        }

        Role userRole = roleService.getRoleByName("ROLE_USER");
        if (userRole == null) {
            userRole = new Role("ROLE_USER");
            roleService.saveRole(userRole);
        }

        if (userService.getUserByUsername("admin") == null) {
            User admin = new User("admin",
                    "100",
                    "admin@mail.ru",
                    Set.of(adminRole, userRole)
            );

            userService.saveOrUpdateUser(admin);
        }

        if (userService.getUserByUsername("user") == null) {
            User user = new User("user",
                    "100",
                    "user@mail.ru",
                    Set.of(userRole)
            );

            userService.saveOrUpdateUser(user);
        }
    }
}