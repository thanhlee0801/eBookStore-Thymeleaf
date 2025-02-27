package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.UserRole;
import com.vn.ebookstore.model.UserRoleId;
import com.vn.ebookstore.repository.UserRoleRepository;
import com.vn.ebookstore.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRoleServiceImpl implements UserRoleService {
    //    Lưu ý:
    //    Vì UserRole có khóa chính composite, nên là sử dụng UserRoleId thay vì int id.

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Override
    public UserRole createUserRole(UserRole userRole) {
        return userRoleRepository.save(userRole);
    }

    @Override
    public UserRole updateUserRole(UserRoleId id, UserRole userRole) {
        UserRole existingUserRole = userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
        existingUserRole.setUser(userRole.getUser());
        existingUserRole.setRole(userRole.getRole());
        return userRoleRepository.save(existingUserRole);
    }

    @Override
    public void deleteUserRole(UserRoleId id) {
        userRoleRepository.deleteById(id);
    }

    @Override
    public UserRole getUserRoleById(UserRoleId id) {
        return userRoleRepository.findById(id).orElseThrow(() -> new RuntimeException("UserRole not found"));
    }

    @Override
    public List<UserRole> getAllUserRoles() {
        return userRoleRepository.findAll();
    }
}