package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
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
import com.example.demo.model.User;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

  @Autowired
  private UserService userService;

  /**
   * 全ユーザー取得
   */
  @GetMapping("/list")
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    List<UserResponseDTO> users = userService.getAllUsers();
    return ResponseEntity.ok(users);
  }

  /**
   * ユーザー登録
   */
  @PostMapping("/register")
  public ResponseEntity<Map<String, Object>> registerUser(
      @Valid @RequestBody UserRegistrationDTO dto,
      BindingResult bindingResult) {

    System.out.println("📩 登録リクエスト受信: " + dto);

    // バリデーションエラーチェック
    if (bindingResult.hasErrors()) {
      System.out.println("❌ バリデーションエラー: " + bindingResult.getErrorCount() + "件");
      
      Map<String, String> errors = new HashMap<>();
      for (FieldError error : bindingResult.getFieldErrors()) {
        System.out.println("  - フィールド: " + error.getField() + 
                         ", 拒否された値: " + error.getRejectedValue() + 
                         ", エラー: " + error.getDefaultMessage());
        errors.put(error.getField(), error.getDefaultMessage());
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "入力内容に誤りがあります");
      response.put("errors", errors);
      
      return ResponseEntity.badRequest().body(response);
    }

    try {
      User user = userService.register(dto);
      System.out.println("✅ ユーザー登録成功: " + user.getUsername());
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "登録成功");
      response.put("user", user);
      
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.err.println("❌ 登録エラー: " + e.getMessage());
      e.printStackTrace();
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "登録に失敗しました: " + e.getMessage());
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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

    System.out.println("📩 更新リクエスト受信: ID=" + id + ", データ=" + dto);

    // バリデーションエラーチェック
    if (bindingResult.hasErrors()) {
      System.out.println("❌ バリデーションエラー: " + bindingResult.getErrorCount() + "件");
      
      Map<String, String> errors = new HashMap<>();
      for (FieldError error : bindingResult.getFieldErrors()) {
        System.out.println("  - フィールド: " + error.getField() + 
                         ", 拒否された値: " + error.getRejectedValue() + 
                         ", エラー: " + error.getDefaultMessage());
        errors.put(error.getField(), error.getDefaultMessage());
      }
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "入力内容に誤りがあります");
      response.put("errors", errors);
      
      return ResponseEntity.badRequest().body(response);
    }

    try {
      userService.updateUser(id, dto);
      System.out.println("✅ ユーザー更新成功: ID=" + id);
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "更新しました");
      
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.err.println("❌ 更新エラー: " + e.getMessage());
      e.printStackTrace();
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "更新に失敗しました: " + e.getMessage());
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * ユーザー検索
   */
  @GetMapping("/search")
  public ResponseEntity<List<UserResponseDTO>> searchUsers(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) Integer age,      // Integer 型で受け取る
      @RequestParam(required = false) String food,
      @RequestParam(defaultValue = "OR") String searchType
  ) {
      try {
          System.out.println("🔍 検索パラメータ:");
          System.out.println("  name: " + name);
          System.out.println("  gender: " + gender);
          System.out.println("  age: " + age);
          System.out.println("  food: " + food);
          System.out.println("  searchType: " + searchType);
          
          UserSearchDTO searchDTO = new UserSearchDTO();
          searchDTO.setName(name);
          searchDTO.setGender(gender);
          searchDTO.setAge(age);
          searchDTO.setFood(food);
          searchDTO.setSearchType(searchType);
          
          List<UserResponseDTO> results = userService.searchUsers(searchDTO);
          
          System.out.println("✅ 検索結果: " + results.size() + "件");
          return ResponseEntity.ok(results);
      } catch (Exception e) {
          System.err.println("❌ 検索エラー: " + e.getMessage());
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
      }
  }

  /**
   * ユーザー削除
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Map<String, Object>> deleteUser(@PathVariable Long id) {
    System.out.println("🗑️ 削除リクエスト: ID=" + id);
    
    try {
      userService.deleteUser(id);
      System.out.println("✅ ユーザー削除成功: ID=" + id);
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "削除しました");
      
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.err.println("❌ 削除エラー: " + e.getMessage());
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "削除に失敗しました: " + e.getMessage());
      
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
  }

  /**
   * ユーザーID取得
   */
  @GetMapping("/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    System.out.println("🔍 ユーザー取得: ID=" + id);
    
    try {
      UserResponseDTO user = userService.getUserById(id);
      System.out.println("✅ ユーザー取得成功: " + user);
      
      return ResponseEntity.ok(user);
    } catch (Exception e) {
      System.err.println("❌ ユーザー取得エラー: " + e.getMessage());
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("message", "ユーザーが見つかりません");
      
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }
  }

  /**
   * ログイン
   */
  @PostMapping("/login")
  public ResponseEntity<Map<String, Object>> login(@RequestBody Map<String, String> credentials) {
    String username = credentials.get("username");
    String password = credentials.get("password");

    System.out.println("🔐 ログイン試行: username=" + username);

    try {
      // LoginRequestDTOを作成
      LoginRequestDTO loginDTO = new LoginRequestDTO();
      loginDTO.setUsername(username);
      loginDTO.setPassword(password);

      User user = userService.login(loginDTO);
      System.out.println("✅ ログイン成功: " + user.getUsername());
      System.out.println("👤 Name: " + user.getName()); 
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", true);
      response.put("message", "ログイン成功");
      response.put("user", Map.of(
        "id", user.getId(),
        "username", user.getUsername(),
        "name", user.getName()
      ));
      
      return ResponseEntity.ok(response);
    } catch (Exception e) {
      System.err.println("❌ ログイン失敗: " + e.getMessage());
      
      Map<String, Object> response = new HashMap<>();
      response.put("success", false);
      response.put("error", e.getMessage());
      
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
  }
}