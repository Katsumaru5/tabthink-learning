package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
    
    @Autowired//どういうルールで読み込まれているか
    private UserService userService;
    
    @PostMapping("/login") //ログイン画面の制御
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        Map<String, Object> response = userService.login(request);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @PostMapping("/register")//ユーザ登録画面の制御
    public ResponseEntity<?> register(@RequestBody Map<String, Object> request) {
        Map<String, Object> response = userService.register(request);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
    
    @GetMapping("/list")//ユーザ一覧画面の制御
    public ResponseEntity<?> getAllUsers() {
        List<Map<String, Object>> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    
    @GetMapping("/search")//ユーザ検索画面の制御
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
    
    @GetMapping("/{id}")//ユーザ詳細画面の制御
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Map<String, Object> user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
    
    @PutMapping("/{id}")//ユーザ編集画面の制御
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Map<String, Object> response = userService.updateUser(id, request);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/{id}")//ユーザ削除画面の制御
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        Map<String, Object> response = userService.deleteUser(id);
        boolean success = (boolean) response.get("success");
        return success ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}