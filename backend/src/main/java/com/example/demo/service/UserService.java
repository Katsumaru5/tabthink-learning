package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> response = new HashMap<>();
        
        boolean success = userRepository.findByUsernameAndDeletedFlag(username, false)
            .map(user -> user.getPassword().equals(password))
            .orElse(false);
        
        if (success) {
            response.put("success", true);
            response.put("message", "ログイン成功");
        } else {
            response.put("success", false);
            response.put("error", "入力情報が間違っています");
        }
        
        return response;
    }
}
