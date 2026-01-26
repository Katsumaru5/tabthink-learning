package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.UserRegistrationDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserSearchDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
@Validated
public class UserController {
    
    @Autowired
    private UserService userService;
    
    // ログイン
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        Map<String, Object> response = userService.login(loginDTO);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    // ユーザ登録
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        Map<String, Object> response = userService.register(registrationDTO);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    // 全ユーザ取得
    @GetMapping("/list")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    // ユーザ検索
    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(
        @RequestParam(required = false) String name,
        @RequestParam(required = false) String gender,
        @RequestParam(required = false) Integer age,
        @RequestParam(required = false) String food,
        @RequestParam(defaultValue = "AND") String searchType
    ) {
        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setName(name);
        searchDTO.setGender(gender);
        searchDTO.setAge(age);
        searchDTO.setFood(food);
        searchDTO.setSearchType(searchType);
        
        List<UserResponseDTO> users = userService.searchUsers(searchDTO);
        return ResponseEntity.ok(users);
    }
    
    // ユーザ詳細取得
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        UserResponseDTO user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    // ユーザ更新
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
        @PathVariable Long id, 
        @Valid @RequestBody UserUpdateDTO updateDTO
    ) {
        Map<String, Object> response = userService.updateUser(id, updateDTO);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    // ユーザ削除（論理削除）
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = userService.deleteUser(id);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}