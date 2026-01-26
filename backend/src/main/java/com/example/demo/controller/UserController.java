package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
import com.example.demo.dto.UserListDTO;
import com.example.demo.dto.UserRegistrationDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserSearchDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 全ユーザー取得（一覧表示用）
     */
    @GetMapping("/list")
    public ResponseEntity<List<UserListDTO>> getAllUsers() {
        List<UserListDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    /**
     * ユーザー登録
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(
            @Valid @RequestBody UserRegistrationDTO dto,
            BindingResult bindingResult) {
        
        // バリデーションエラーチェック
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(buildValidationErrorResponse(bindingResult));
        }

        try {
            Map<String, Object> response = userService.registerUser(dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(e.getMessage()));
        }
    }

    /**
     * ユーザー情報更新
     */
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateDTO dto,
            BindingResult bindingResult) {

        // バリデーションエラーチェック
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                .body(buildValidationErrorResponse(bindingResult));
        }

        try {
            Map<String, Object> response = userService.updateUser(id, dto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(e.getMessage()));
        }
    }

    /**
     * ユーザー検索
     */
    @GetMapping("/search")
    public ResponseEntity<List<UserListDTO>> searchUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String food,
            @RequestParam(defaultValue = "OR") String searchType) {
        
        UserSearchDTO searchDTO = new UserSearchDTO();
        searchDTO.setName(name);
        searchDTO.setGender(gender);
        searchDTO.setAge(age);
        searchDTO.setFood(food);
        searchDTO.setSearchType(searchType);
        
        List<UserListDTO> results = userService.searchUsers(searchDTO);
        return ResponseEntity.ok(results);
    }

    /**
     * ユーザー削除
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
        try {
            Map<String, Object> response = userService.deleteUser(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildErrorResponse(e.getMessage()));
        }
    }

    /**
     * ユーザーID取得
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponseDTO user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildErrorResponse("ユーザーが見つかりません"));
        }
    }

    /**
     * ログイン
     */
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> credentials) {
        
        String username = credentials.get("username");
        String password = credentials.get("password");
        
        LoginRequestDTO loginDTO = new LoginRequestDTO();
        loginDTO.setUsername(username);
        loginDTO.setPassword(password);
        
        try {
            Map<String, Object> response = userService.login(loginDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    // ===== プライベートヘルパーメソッド =====

    /**
     * バリデーションエラーレスポンス構築
     */
    private Map<String, Object> buildValidationErrorResponse(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        bindingResult.getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", "入力内容に誤りがあります");
        response.put("errors", errors);
        
        return response;
    }

    /**
     * エラーレスポンス構築
     */
    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}