package com.boot.smart.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity  // For web layer (URL-based) security
@EnableMethodSecurity  // For method-level security
public class SecurityConfig{
	
	@Bean
	public UserDetailsService getUserDetailsService() {
		return new CustomUserDetailsService();
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	public DaoAuthenticationProvider daoAuthenticationProvider()
	{
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());
		
		return daoAuthenticationProvider;
	}
	
	// Updated SecurityFilterChain using authorizeHttpRequests()
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        	.authorizeHttpRequests(auth -> auth
                .requestMatchers("/admin/**").hasRole("ADMIN")  // Restrict /admin/** to ROLE_ADMIN
                .requestMatchers("/user/**").hasRole("USER")  // Restrict /users/** to ROLE_USER
                .anyRequest().permitAll()  // Allow access to all other pages
            )
        	.formLogin(form -> form
                    .loginPage("/custom-login")
                    .loginProcessingUrl("/handleLogin")
                    .defaultSuccessUrl("/user/dashboard")
                    .permitAll()  // Allow all users to access the login page
                )
                .logout(logout -> logout
                	.permitAll()
                )
                .csrf(csrf -> csrf.disable());  // Disable CSRF protection
            return http.build();
    }

    // Exposing the AuthenticationManager bean
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
