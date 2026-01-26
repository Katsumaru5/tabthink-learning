package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.LoginRequestDTO;
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
  
  // ログイン処理
  public User login(LoginRequestDTO loginDTO) {
    // ユーザー検索
    Optional<User> userOpt = userRepository.findByUsernameAndDeletedFlag(
      loginDTO.getUsername(), 
      false
    );
    
    // ユーザー存在確認とパスワード照合
    if (userOpt.isPresent()) {
      User user = userOpt.get();
      if (user.getPassword().equals(loginDTO.getPassword())) {
        return user;  // Userオブジェクトを返す
      }
    }
    
    // ログイン失敗時は例外をスロー
    throw new RuntimeException("入力情報が間違っています");
  }
  
  // ユーザ登録処理
  @Transactional
  public User register(UserRegistrationDTO registrationDTO) {
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
    
    return savedUser;  // Userオブジェクトを返す
  }
  
  // ユーザ更新処理
  @Transactional
  public void updateUser(Long userId, UserUpdateDTO updateDTO) {
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
  }
  
  // ユーザ取得処理
  public UserResponseDTO getUserById(Long userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    
    if (!userOpt.isPresent() || userOpt.get().getDeletedFlag()) {
      return null;
    }
    
    return convertToResponseDTO(userOpt.get());
  }
  
  // ユーザ削除処理（論理削除）
  @Transactional
  public void deleteUser(Long userId) {
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
  }
  
  // 全ユーザ取得処理
  public List<UserResponseDTO> getAllUsers() {
    List<User> users = userRepository.findAllActiveUsersWithFoods();
    
    return users.stream()
      .map(this::convertToResponseDTO)
      .collect(Collectors.toList());
  }
  
  // ユーザ検索処理
  public List<UserResponseDTO> searchUsers(UserSearchDTO searchDTO) {
    List<User> users;
    
    // 食べ物検索があるか判定
    boolean hasFoodSearch = searchDTO.getFood() != null && !searchDTO.getFood().isEmpty();
    
    if ("OR".equalsIgnoreCase(searchDTO.getSearchType())) {
      // OR検索の場合は全データ取得後にアプリケーション側でフィルタリング
      users = userRepository.findAllActiveUsersWithFoods();
      users = filterUsersWithOr(users, searchDTO);
    } else {
      // AND検索の場合はSQLで絞り込み
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
    
    return users.stream()
      .map(this::convertToResponseDTO)
      .collect(Collectors.toList());
  }
  
  // ===== プライベートメソッド =====
  
  // 好きな食べ物を保存する共通処理
  private void saveFavoriteFoods(User user, String favoriteFoodsStr) {
    if (favoriteFoodsStr == null || favoriteFoodsStr.trim().isEmpty()) {
      return;
    }
    
    String[] foods = favoriteFoodsStr.split("[,、]");
    List<FavoriteFood> favoriteFoodList = new ArrayList<>();
    
    for (String food : foods) {
      String trimmedFood = food.trim();
      if (!trimmedFood.isEmpty()) {
        FavoriteFood favoriteFood = new FavoriteFood(user, trimmedFood);
        favoriteFoodList.add(favoriteFood);
      }
    }
    
    // バルクインサートで一括保存
    if (!favoriteFoodList.isEmpty()) {
      favoriteFoodRepository.saveAll(favoriteFoodList);
    }
  }
  
  // OR検索のフィルタリング
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
  
  // UserエンティティをUserResponseDTOに変換
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