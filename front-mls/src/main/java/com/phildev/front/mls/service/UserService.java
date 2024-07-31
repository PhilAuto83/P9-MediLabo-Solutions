package com.phildev.front.mls.service;

import com.phildev.front.mls.model.User;
import com.phildev.front.mls.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public User findByEmail(String email){
        return Optional.ofNullable(userRepository.findByEmail(email)).orElseThrow(()->new RuntimeException("User not found with email :"+email));
    }


}