package com.vn.ebookstore.config;

import com.vn.ebookstore.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((authorize) -> 
                authorize
                    // Các URL công khai cho tất cả người dùng (bao gồm cả guest)
                    .requestMatchers("/register", "/login", "/css/**", "/js/**", "/image/**").permitAll()
                    
                    // Các URL không yêu cầu đăng nhập
                    .requestMatchers("/", "/about_us", "/products", "/category/**", "/book/**", "/faq", "/order_tracking").permitAll()
                    
                    // Các URL dành cho admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    
                    // Các URL còn lại yêu cầu đăng nhập
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .successHandler((request, response, authentication) -> {
                    if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                        response.sendRedirect("/admin/dashboard");
                    } else {
                        // Chuyển hướng về trang chủ của user sau khi đăng nhập
                        response.sendRedirect("/user/home");
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")  // Chuyển về trang chủ sau khi đăng xuất
                .permitAll()
            )
            .csrf(csrf -> csrf.disable()); // Tạm thời tắt CSRF trong quá trình phát triển
        
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = 
            http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
            .userDetailsService((UserDetailsService) userService)
            .passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }
}
