package com.example.demo.service;

import com.example.demo.model.Role;

public interface RoleService {
    Role getRoleByName(String name);
    void saveRole(Role role);
}