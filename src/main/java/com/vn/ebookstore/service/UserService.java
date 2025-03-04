package com.vn.ebookstore.service;

import com.vn.ebookstore.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user);
    User updateUser(int id, User user);
    void deleteUser(int id);
    User getUserById(int id);
    List<User> getAllUsers();
    
    // Thêm các phương thức mới
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User registerNewUser(User user);
    boolean checkEmailExists(String email);
    boolean checkUsernameExists(String username);
}