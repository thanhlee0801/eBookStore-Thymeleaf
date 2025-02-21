package com.vn.ebookstore.service;

import com.vn.ebookstore.model.User;
import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(int id, User user);
    void deleteUser(int id);
    User getUserById(int id);
    List<User> getAllUsers();
}