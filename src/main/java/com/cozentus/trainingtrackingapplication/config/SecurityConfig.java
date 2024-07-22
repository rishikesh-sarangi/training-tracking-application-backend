package com.cozentus.trainingtrackingapplication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cozentus.trainingtrackingapplication.filter.JwtAuthenticationFilter;
import com.cozentus.trainingtrackingapplication.service.MyUserService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)

public class SecurityConfig {

	private MyUserService myUserService;

	private JwtAuthenticationFilter jwtAuthenticationFilter;

	SecurityConfig(MyUserService myUserService, JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.myUserService = myUserService;
	}

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authenticationProvider(authenticationProvider());
		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/users/login").permitAll()
						.requestMatchers("/users/**").hasRole("ADMIN").requestMatchers("/attendance/**")
						.hasRole("TEACHER").requestMatchers("/evaluations/**").hasAnyRole("TEACHER", "ADMIN")
						.requestMatchers("/evaluationStudent/**").hasRole("TEACHER").requestMatchers("/enrollments/**")
						.hasRole("ADMIN").requestMatchers("/batches/**").hasAnyRole("ADMIN", "TEACHER")
						.requestMatchers("/programs/**").hasRole("ADMIN").requestMatchers("/batch-course-teacher/**")
						.hasAnyRole("ADMIN", "TEACHER").requestMatchers("/students/**").hasAnyRole("ADMIN", "TEACHER")
						.requestMatchers("/teachers/**").hasAnyRole("ADMIN", "TEACHER").requestMatchers("/topics/**")
						.hasAnyRole("ADMIN", "TEACHER").requestMatchers("/courses/**").hasAnyRole("ADMIN", "TEACHER")
						.anyRequest().authenticated())
				.httpBasic(Customizer.withDefaults());
				http.addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	UserDetailsService userDetailsService() {
		return myUserService;
	}

	@Bean
	DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		authProvider.setHideUserNotFoundExceptions(false);
		return authProvider;
	}

	@Bean
	AuthenticationManager authenticationManager() {
		return new ProviderManager(authenticationProvider());
	}
}
