package com.vn.ebookstore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "user_roles")
public class UserRole {
    @Id
    private int userId;
    @Id
    private int roleId;

    // Getters and Setters
}