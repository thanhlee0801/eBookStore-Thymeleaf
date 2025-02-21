package com.vn.ebookstore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
@IdClass(UserRoleId.class)
public class UserRole {
    @Id
    private int userId;

    @Id
    private int roleId;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }
}