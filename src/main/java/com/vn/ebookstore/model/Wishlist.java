package com.vn.ebookstore.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "wishlist")
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    private int bookId;
    @Column(updatable = false)
    private Date createdAt;
    private Date deletedAt;

    // Getters and Setters
}