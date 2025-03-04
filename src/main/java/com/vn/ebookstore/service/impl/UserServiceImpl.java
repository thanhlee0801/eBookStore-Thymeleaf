package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Role;
import com.vn.ebookstore.model.User;
import com.vn.ebookstore.repository.RoleRepository;
import com.vn.ebookstore.repository.UserRepository;
import com.vn.ebookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy user với email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles())
        );
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName().toUpperCase()))
                .collect(Collectors.toList());
    }

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User updateUser(int id, User user) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setUsername(user.getUsername());
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(user.getPassword());
        existingUser.setBirthOfDate(user.getBirthOfDate());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        return userRepository.save(existingUser);
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User registerNewUser(User user) {
        // Kiểm tra email và username đã tồn tại chưa
        if (checkEmailExists(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }
        if (checkUsernameExists(user.getUsername())) {
            throw new RuntimeException("Username đã tồn tại");
        }

        // Mã hóa mật khẩu
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Set ngày tạo
        user.setCreatedAt(new Date());
        
        // Set role mặc định là CUSTOMER
        Role customerRole = roleRepository.findByName("customer")
                .orElseThrow(() -> new RuntimeException("Role 'customer' không tồn tại"));
        user.setRoles(Arrays.asList(customerRole));

        return userRepository.save(user);
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }
}