package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Role;
import java.util.List;

public interface RoleService {
    Role createRole(Role role);
    Role updateRole(int id, Role role);
    void deleteRole(int id);
    Role getRoleById(int id);
    List<Role> getAllRoles();
}