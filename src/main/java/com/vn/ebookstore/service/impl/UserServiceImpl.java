package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Role;
import com.vn.ebookstore.model.User;
import com.vn.ebookstore.model.Address;
import com.vn.ebookstore.repository.RoleRepository;
import com.vn.ebookstore.repository.UserRepository;
import com.vn.ebookstore.repository.AddressRepository;
import com.vn.ebookstore.security.UserDetailsImpl;
import com.vn.ebookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AddressRepository addressRepository;

    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        logger.info("Đang tìm user với email: {}", email);
        
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("Không tìm thấy user với email: {}", email);
                    return new UsernameNotFoundException("Không tìm thấy user với email: " + email);
                });

        logger.info("Tìm thấy user: {}", user.getEmail());
        logger.info("Mật khẩu trong database: {}", user.getPassword());
        
        // Load roles một cách eager
        Collection<Role> roles = user.getRoles();
        logger.info("Roles của user: {}", roles.stream().map(Role::getName).collect(Collectors.joining(", ")));

        return new UserDetailsImpl(user, mapRolesToAuthorities(roles));
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

        // Lưu user trước để có ID
        user = userRepository.save(user);

        // Lưu địa chỉ nếu có
        if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
            for (Address address : user.getAddresses()) {
                address.setUser(user);
                address.setCreatedAt(new Date());
                addressRepository.save(address);
            }
        }

        return user;
    }

    @Override
    public boolean checkEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    public boolean checkUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        try {
            logger.info("Kiểm tra mật khẩu - Raw: {}, Encoded: {}", rawPassword, encodedPassword);
            
            // Thử so sánh trực tiếp nếu mật khẩu chưa mã hóa
            if (rawPassword.equals(encodedPassword)) {
                logger.info("Mật khẩu khớp (chưa mã hóa)");
                return true;
            }
            
            // Nếu không khớp, thử so sánh với mật khẩu đã mã hóa
            boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
            logger.info("Kết quả so sánh mật khẩu đã mã hóa: {}", matches);
            return matches;
        } catch (Exception e) {
            logger.error("Lỗi khi kiểm tra mật khẩu: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy user với email: " + email));
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

}