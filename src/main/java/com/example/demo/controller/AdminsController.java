package com.example.demo.controller;

import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.service.RoleService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminsController {

    private final UserService userService;
    private final RoleService roleService;

    @GetMapping
    public String adminPanel(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "admin/index";
    }

    @GetMapping("/new")
    public String newUserForm(Model model) {
        model.addAttribute("user", new User());
        return "admin/user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user, @RequestParam(required = false) String roleName) {
        if (roleName != null && !roleName.isEmpty()) {
            Role role = roleService.getRoleByName(roleName);
            if (role != null) {
                user.getRoles().add(role);
            }
        }
        userService.saveOrUpdateUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        user.setPassword(null);
        model.addAttribute("user", user);
        return "admin/user-form";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin";
    }
}