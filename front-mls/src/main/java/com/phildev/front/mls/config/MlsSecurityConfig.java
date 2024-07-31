package com.phildev.front.mls.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class MlsSecurityConfig {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    /***
     * This bean is used to manage authorized and public endpoints
     * It has a session management layer with maximum 1 session per user
     * @param http is the root object from which we can build our custom security filter chain
     * @return SecurityFilterChain object used to filter request and add security layer
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(auth ->{
                    auth.requestMatchers( "/login","/js/**","/css/**","/images/**", "/actuator/**").permitAll();
                    auth.requestMatchers("/admin", "/utilisateurs/liste").hasRole("ADMIN");
                    auth.requestMatchers("/home").hasRole("USER");
                    auth.anyRequest().authenticated();
                }).formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(new CustomSuccessHandler())
                        .failureHandler(new CustomAuthenticationFailure()))
                  .sessionManagement(session -> session.maximumSessions(1)
                        .maxSessionsPreventsLogin(true))
                .build();
    }

    /***
     * This bean is used to publish event about session expired or session created
     * It will be used to prevent user to be logged twice due to security filter chain config
     * @return an HttpSessionEventPublisher
     */
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }


}
