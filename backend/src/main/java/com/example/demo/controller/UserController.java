package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = userService.login(
            request.get("username"), 
            request.get("password")
        );
        
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {
        User user = new User();
        user.setUsername((String) request.get("username"));
        user.setPassword((String) request.get("password"));
        user.setName((String) request.get("name"));
        user.setGender((String) request.get("gender"));
        
        if (request.get("age") != null) {
            user.setAge(Integer.parseInt(request.get("age").toString()));
        }
        
        user.setPostalCode((String) request.get("postalCode"));
        user.setPrefecture((String) request.get("prefecture"));
        user.setCity((String) request.get("city"));
        user.setAddress((String) request.get("address"));
        user.setPhoneNumber((String) request.get("phoneNumber"));
        user.setNationality((String) request.get("nationality"));
        
        @SuppressWarnings("unchecked")
        List<String> favoriteFoods = (List<String>) request.get("favoriteFoods");
        
        Map<String, Object> response = userService.register(user, favoriteFoods);
        
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/list")
    public ResponseEntity<?> getAllUsers() {
        List<Map<String, Object>> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")
    public ResponseEntity<?> searchUsers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) Integer age,
        @RequestParam(required = false) String food,
        @RequestParam(defaultValue = "AND") String searchType
    ) {
        List<Map<String, Object>> users = userService.searchUsers(name, gender, age, food, searchType);
        return ResponseEntity.ok(users);
    }
}