package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LoginRequestDTO;
import com.example.demo.dto.UserListDTO;
import com.example.demo.dto.UserRegistrationDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.dto.UserSearchDTO;
import com.example.demo.dto.UserUpdateDTO;
import com.example.demo.model.FavoriteFood;
import com.example.demo.model.User;
import com.example.demo.repository.FavoriteFoodRepository;
import com.example.demo.repository.UserRepository;

@Service
public class UserService {
  
  @Autowired
  private UserRepository userRepository;
  
  @Autowired
  private FavoriteFoodRepository favoriteFoodRepository;
  
  /**
   * ログイン処理
   */
  public Map<String, Object> login(LoginRequestDTO loginDTO) {
    System.out.println("🔐 ログイン試行: username=" + loginDTO.getUsername());
    
    Optional<User> userOpt = userRepository.findByUsernameAndDeletedFlag(
      loginDTO.getUsername(), 
      false
    );
    
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (user.getPassword().equals(loginDTO.getPassword())) {
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
        
        return response;
      }
    }
    
    System.err.println("❌ ログイン失敗: 入力情報が間違っています");
    throw new RuntimeException("入力情報が間違っています");
  }
  
  /**
   * ユーザ登録処理
   */
  @Transactional
  public Map<String, Object> registerUser(UserRegistrationDTO registrationDTO) {
    System.out.println("📩 登録リクエスト受信: " + registrationDTO);
    
    // ユーザー名の重複チェック
    Optional<User> existingUser = userRepository.findByUsernameAndDeletedFlag(
      registrationDTO.getUsername(), 
      false
    );
    
    if (existingUser.isPresent()) {
      throw new RuntimeException("このユーザー名は既に使用されています");
    }
    
    // DTOからエンティティへ変換
    User user = new User();
    user.setUsername(registrationDTO.getUsername());
    user.setPassword(registrationDTO.getPassword());
    user.setName(registrationDTO.getName());
    user.setGender(registrationDTO.getGender());
    user.setAge(registrationDTO.getAge());
    user.setPostalCode(registrationDTO.getPostalCode());
    user.setPrefecture(registrationDTO.getPrefecture());
    user.setCity(registrationDTO.getCity());
    user.setAddress(registrationDTO.getAddress());
    user.setPhoneNumber(registrationDTO.getPhoneNumber());
    user.setNationality(registrationDTO.getNationality());
    
    // ユーザーを保存
    User savedUser = userRepository.save(user);
    
    // 好きな食べ物を保存
    List<String> favoriteFoodsList = registrationDTO.getFavoriteFoods();
    if (favoriteFoodsList != null && !favoriteFoodsList.isEmpty()) {
      for (String foodName : favoriteFoodsList) {
        if (foodName != null && !foodName.trim().isEmpty()) {
          FavoriteFood food = new FavoriteFood(savedUser, foodName.trim());
          favoriteFoodRepository.save(food);
        }
      }
    }
    
    System.out.println("✅ ユーザー登録成功: " + savedUser.getUsername());
    
    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "登録成功");
    response.put("user", savedUser);
    
    return response;
  }
  
  /**
   * ユーザ更新処理
   */
  @Transactional
  public Map<String, Object> updateUser(Long userId, UserUpdateDTO updateDTO) {
    System.out.println("📩 更新リクエスト受信: ID=" + userId + ", データ=" + updateDTO);
    
    Optional<User> userOpt = userRepository.findById(userId);
    if (!userOpt.isPresent()) {
      throw new RuntimeException("ユーザーが見つかりません");
    }
    
    User user = userOpt.get();
    
    // DTOからエンティティへ更新
    user.setName(updateDTO.getName());
    user.setGender(updateDTO.getGender());
    user.setAge(updateDTO.getAge());
    user.setPostalCode(updateDTO.getPostalCode());
    user.setPrefecture(updateDTO.getPrefecture());
    user.setCity(updateDTO.getCity());
    user.setAddress(updateDTO.getAddress());
    user.setPhoneNumber(updateDTO.getPhoneNumber());
    user.setNationality(updateDTO.getNationality());
    
    // 新しい好きな食べ物を追加
    List<String> favoriteFoodsList = updateDTO.getFavoriteFoods();
    if (favoriteFoodsList != null) {
      // 既存の好物を論理削除
      favoriteFoodRepository.logicalDeleteByUser(user);
      
      // 新しい好物を保存
      for (String foodName : favoriteFoodsList) {
        if (foodName != null && !foodName.trim().isEmpty()) {
          FavoriteFood food = new FavoriteFood(user, foodName.trim());
          favoriteFoodRepository.save(food);
        }
      }
    }
    
    userRepository.save(user);
    
    System.out.println("✅ ユーザー更新成功: ID=" + userId);
    
    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "更新しました");
    
    return response;
  }
  
  /**
   * ユーザ取得処理
   */
  public UserResponseDTO getUserById(Long userId) {
    System.out.println("🔍 ユーザー取得: ID=" + userId);
    
    Optional<User> userOpt = userRepository.findById(userId);
    
    if (!userOpt.isPresent() || userOpt.get().getDeletedFlag()) {
      throw new RuntimeException("ユーザーが見つかりません");
    }
    
    UserResponseDTO dto = convertToResponseDTO(userOpt.get());
    System.out.println("✅ ユーザー取得成功: " + dto);
    
    return dto;
  }
  
  /**
   * ユーザ削除処理（論理削除）
   */
  @Transactional
  public Map<String, Object> deleteUser(Long userId) {
    System.out.println("🗑️ 削除リクエスト: ID=" + userId);
    
    Optional<User> userOpt = userRepository.findById(userId);
    if (!userOpt.isPresent()) {
      throw new RuntimeException("ユーザーが見つかりません");
    }
    
    User user = userOpt.get();
    
    // ユーザーの論理削除
    user.setDeletedFlag(true);
    userRepository.save(user);
    
    // 好きな食べ物も論理削除
    favoriteFoodRepository.logicalDeleteByUser(user);
    
    System.out.println("✅ ユーザー削除成功: ID=" + userId);
    
    Map<String, Object> response = new HashMap<>();
    response.put("success", true);
    response.put("message", "削除しました");
    
    return response;
  }
  
  /**
   * 全ユーザ取得処理（一覧表示用）
   */
  public List<UserListDTO> getAllUsers() {
    System.out.println("📋 ユーザー一覧取得開始");
    
    // 必要なフィールドのみ取得
    List<UserListDTO> users = userRepository.findAllUsersForList();
    
    if (users.isEmpty()) {
      System.out.println("✅ ユーザー一覧取得成功: 0件");
      return users;
    }
    
    // ユーザーIDのリストを作成
    List<Long> userIds = users.stream()
        .map(UserListDTO::getId)
        .collect(Collectors.toList());
    
    // 好きな食べ物を一括取得
    List<Object[]> favoriteFoodsData = userRepository.findFavoriteFoodsByUserIds(userIds);
    
    // ユーザーIDごとに好きな食べ物をグループ化
    Map<Long, List<String>> foodsMap = favoriteFoodsData.stream()
        .collect(Collectors.groupingBy(
            row -> (Long) row[0],
            Collectors.mapping(row -> (String) row[1], Collectors.toList())
        ));
    
    // 各ユーザーに好きな食べ物を設定
    for (UserListDTO user : users) {
      List<String> foods = foodsMap.getOrDefault(user.getId(), new ArrayList<>());
      user.setFavoriteFoods(foods);
    }
    
    System.out.println("✅ ユーザー一覧取得成功: " + users.size() + "件");
    return users;
  }
  
  /**
   * ユーザ検索処理
   */
  public List<UserListDTO> searchUsers(UserSearchDTO searchDTO) {
    System.out.println("🔍 検索パラメータ:");
    System.out.println("  name: " + searchDTO.getName());
    System.out.println("  gender: " + searchDTO.getGender());
    System.out.println("  age: " + searchDTO.getAge());
    System.out.println("  food: " + searchDTO.getFood());
    System.out.println("  searchType: " + searchDTO.getSearchType());
    
    List<User> users;
    
    // 食べ物検索があるか判定
    boolean hasFoodSearch = searchDTO.getFood() != null && !searchDTO.getFood().isEmpty();
    
    if ("OR".equalsIgnoreCase(searchDTO.getSearchType())) {
      // OR検索の場合
      users = userRepository.findAllActiveUsersWithFoods();
      users = filterUsersWithOr(users, searchDTO);
    } else {
      // AND検索の場合
      if (hasFoodSearch) {
        users = userRepository.searchUsersAndWithFood(
          searchDTO.getName(),
          searchDTO.getGender(),
          searchDTO.getAge(),
          searchDTO.getFood()
        );
      } else {
        users = userRepository.searchUsersAnd(
          searchDTO.getName(),
          searchDTO.getGender(),
          searchDTO.getAge()
        );
      }
    }
    
    List<UserListDTO> results = users.stream()
        .map(this::convertToListDTO)
        .collect(Collectors.toList());
    
    System.out.println("✅ 検索結果: " + results.size() + "件");
    return results;
  }
  
  // ===== プライベートメソッド =====
  
  /**
   * OR検索のフィルタリング
   */
  private List<User> filterUsersWithOr(List<User> users, UserSearchDTO searchDTO) {
    return users.stream()
      .filter(user -> {
        // 全ての条件がnullまたは空の場合は全件返す
        boolean allEmpty = 
          (searchDTO.getName() == null || searchDTO.getName().isEmpty()) &&
          (searchDTO.getGender() == null || searchDTO.getGender().isEmpty()) &&
          searchDTO.getAge() == null &&
          (searchDTO.getFood() == null || searchDTO.getFood().isEmpty());
        
        if (allEmpty) {
          return true;
        }
        
        // 各条件をチェック
        boolean nameMatch = searchDTO.getName() != null && 
          !searchDTO.getName().isEmpty() && 
          user.getName().contains(searchDTO.getName());
          
        boolean genderMatch = searchDTO.getGender() != null && 
          !searchDTO.getGender().isEmpty() && 
          searchDTO.getGender().equals(user.getGender());
          
        boolean ageMatch = searchDTO.getAge() != null && 
          user.getAge() != null && 
          user.getAge().equals(searchDTO.getAge());
          
        boolean foodMatch = searchDTO.getFood() != null && 
          !searchDTO.getFood().isEmpty() && 
          user.getFavoriteFoods().stream()
            .filter(f -> !f.getDeletedFlag())
            .anyMatch(f -> f.getFoodName().contains(searchDTO.getFood()));
        
        // OR条件（いずれか一つでも一致すればtrue）
        return nameMatch || genderMatch || ageMatch || foodMatch;
      })
      .collect(Collectors.toList());
  }
  
  /**
   * User を UserListDTO に変換（軽量版）
   */
  private UserListDTO convertToListDTO(User user) {
    UserListDTO dto = new UserListDTO();
    dto.setId(user.getId());
    dto.setName(user.getName());
    dto.setGender(user.getGender());
    dto.setAge(user.getAge());
    dto.setPostalCode(user.getPostalCode());
    dto.setPrefecture(user.getPrefecture());
    dto.setCity(user.getCity());
    dto.setAddress(user.getAddress());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setNationality(user.getNationality());
    
    // 好きな食べ物
    List<String> foods = user.getFavoriteFoods().stream()
        .filter(f -> !f.getDeletedFlag())
        .map(FavoriteFood::getFoodName)
        .collect(Collectors.toList());
    dto.setFavoriteFoods(foods);
    
    return dto;
  }
  
  /**
   * UserエンティティをUserResponseDTOに変換
   */
  private UserResponseDTO convertToResponseDTO(User user) {
    UserResponseDTO dto = new UserResponseDTO();
    dto.setId(user.getId());
    dto.setUsername(user.getUsername());
    dto.setName(user.getName());
    dto.setGender(user.getGender());
    dto.setAge(user.getAge());
    dto.setPostalCode(user.getPostalCode());
    dto.setPrefecture(user.getPrefecture());
    dto.setCity(user.getCity());
    dto.setAddress(user.getAddress());
    dto.setPhoneNumber(user.getPhoneNumber());
    dto.setNationality(user.getNationality());
    
    // 論理削除されていない食べ物のみ取得
    List<String> foods = user.getFavoriteFoods().stream()
        .filter(f -> !f.getDeletedFlag())
        .map(FavoriteFood::getFoodName)
        .collect(Collectors.toList());
    dto.setFavoriteFoods(foods);
    
    return dto;
  }
}