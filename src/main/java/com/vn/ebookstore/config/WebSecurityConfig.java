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
                    // Resources tĩnh
                    .requestMatchers(
                        "/css/**", 
                        "/js/**", 
                        "/image/**", 
                        "/vendor/**", 
                        "/fonts/**"
                    ).permitAll()
                    
                    // Trang xác thực
                    .requestMatchers("/login", "/register").permitAll()
                    
                    // Trang công khai (guest)
                    .requestMatchers(
                        "/",                // Trang chủ
                        "/about_us",        // Về chúng tôi
                        "/products",        // Danh sách sản phẩm
                        "/product/*",       // Chi tiết sản phẩm
                        "/category/*",      // Danh mục
                        "/faq"             // FAQ
                    ).permitAll()
                    
                    // Trang admin
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    
                    // Trang user (yêu cầu đăng nhập)
                    .requestMatchers(
                        "/user/**",         // Trang cá nhân
                        "/cart/**",         // Giỏ hàng
                        "/checkout/**",     // Thanh toán
                        "/wishlist/**",     // Yêu thích
                        "/order/**",        // Đơn hàng
                        "/review/**"        // Đánh giá
                    ).authenticated()
                    
                    // Mặc định
                    .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/user/home", true)  // Thêm true để force redirect
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID", "remember-me")
                .permitAll()
            )
            .rememberMe(remember -> remember
                .key("uniqueAndSecretKey")
                .tokenValiditySeconds(86400) // 24 giờ
                .rememberMeParameter("rememberMe")
                .userDetailsService((UserDetailsService) userService)
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .maximumSessions(1)
                .expiredUrl("/login?expired=true")
            );
        
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
