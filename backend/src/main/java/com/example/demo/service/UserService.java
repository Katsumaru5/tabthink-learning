package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findByUsernameAndDeletedFlag(username, false);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPassword().equals(password)) {
                response.put("success", true);
                response.put("message", "ログイン成功");
                response.put("name", user.getName());
                return response;
            }
        }
        
        response.put("success", false);
        response.put("error", "入力情報が間違っています");
        return response;
    }
    
    @Transactional
    public Map<String, Object> register(User user, List<String> foodNames) {
        Map<String, Object> response = new HashMap<>();
        
        // ユーザー名の重複チェック
        Optional<User> existingUser = userRepository.findByUsernameAndDeletedFlag(
            user.getUsername(), 
            false
        );
        
        if (existingUser.isPresent()) {
            response.put("success", false);
            response.put("error", "このユーザー名は既に使用されています");
            return response;
        }
        
        // ユーザーを保存
        User savedUser = userRepository.save(user);
        
        // 好きな食べ物を保存（カンマ区切りで分割）
        if (foodNames != null && !foodNames.isEmpty()) {
            for (String foodName : foodNames) {
                if (foodName != null && !foodName.trim().isEmpty()) {
                    // カンマで分割して個別に保存
                    String[] foods = foodName.split("[,、]");
                    for (String food : foods) {
                        String trimmedFood = food.trim();
                        if (!trimmedFood.isEmpty()) {
                            FavoriteFood favoriteFood = new FavoriteFood(savedUser, trimmedFood);
                            favoriteFoodRepository.save(favoriteFood);
                        }
                    }
                }
            }
        }
        
        response.put("success", true);
        response.put("message", "登録成功");
        return response;
    }
    
    @Transactional
    public Map<String, Object> updateUser(Long userId, User updatedUser, List<String> foodNames) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            response.put("success", false);
            response.put("error", "ユーザーが見つかりません");
            return response;
        }
        
        User user = userOpt.get();
        
        // ユーザー情報を更新
        user.setName(updatedUser.getName());
        user.setGender(updatedUser.getGender());
        user.setAge(updatedUser.getAge());
        user.setPostalCode(updatedUser.getPostalCode());
        user.setPrefecture(updatedUser.getPrefecture());
        user.setCity(updatedUser.getCity());
        user.setAddress(updatedUser.getAddress());
        user.setPhoneNumber(updatedUser.getPhoneNumber());
        user.setNationality(updatedUser.getNationality());
        
        // 既存の好きな食べ物を削除
        favoriteFoodRepository.deleteAll(user.getFavoriteFoods());
        user.getFavoriteFoods().clear();
        
        // 新しい好きな食べ物を追加（カンマ区切りで分割）
        if (foodNames != null && !foodNames.isEmpty()) {
            for (String foodName : foodNames) {
                if (foodName != null && !foodName.trim().isEmpty()) {
                    // カンマで分割して個別に保存
                    String[] foods = foodName.split("[,、]");
                    for (String food : foods) {
                        String trimmedFood = food.trim();
                        if (!trimmedFood.isEmpty()) {
                            FavoriteFood favoriteFood = new FavoriteFood(user, trimmedFood);
                            user.getFavoriteFoods().add(favoriteFood);
                        }
                    }
                }
            }
        }
        
        userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "ユーザー情報を更新しました");
        return response;
    }

    public Map<String, Object> getUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        
        if (!userOpt.isPresent()) {
            return null;
        }
        
        User user = userOpt.get();
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("name", user.getName());
        userMap.put("gender", user.getGender());
        userMap.put("age", user.getAge());
        userMap.put("postalCode", user.getPostalCode());
        userMap.put("prefecture", user.getPrefecture());
        userMap.put("city", user.getCity());
        userMap.put("address", user.getAddress());
        userMap.put("phoneNumber", user.getPhoneNumber());
        userMap.put("nationality", user.getNationality());
        
        List<String> foods = user.getFavoriteFoods().stream()
            .map(FavoriteFood::getFoodName)
            .collect(Collectors.toList());
        userMap.put("favoriteFoods", foods);
        
        return userMap;
    }

    @Transactional
    public Map<String, Object> deleteUser(Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        Optional<User> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            response.put("success", false);
            response.put("error", "ユーザーが見つかりません");
            return response;
        }
        
        User user = userOpt.get();
        user.setDeletedFlag(true);
        userRepository.save(user);
        
        response.put("success", true);
        response.put("message", "削除成功");
        return response;
    }
    
    public List<Map<String, Object>> getAllUsers() {
        List<User> users = userRepository.findAllWithFavoriteFoods();
        
        return users.stream().map(user -> {
            Map<String, Object> userMap = new HashMap<>();
            userMap.put("id", user.getId());
            userMap.put("username", user.getUsername());
            userMap.put("name", user.getName());
            userMap.put("gender", user.getGender());
            userMap.put("age", user.getAge());
            userMap.put("postalCode", user.getPostalCode());
            userMap.put("prefecture", user.getPrefecture());
            userMap.put("city", user.getCity());
            userMap.put("address", user.getAddress());
            userMap.put("phoneNumber", user.getPhoneNumber());
            userMap.put("nationality", user.getNationality());
            
            List<String> foods = user.getFavoriteFoods().stream()
                .map(FavoriteFood::getFoodName)
                .collect(Collectors.toList());
            userMap.put("favoriteFoods", foods);
            
            return userMap;
        }).collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> searchUsers(String name, String gender, Integer age, String food, String searchType) {
        List<User> allUsers = userRepository.findAllWithFavoriteFoods();
        
        return allUsers.stream()
            .filter(user -> {
                boolean nameMatch = name == null || name.isEmpty() || user.getName().contains(name);
                boolean genderMatch = gender == null || gender.isEmpty() || gender.equals(user.getGender());
                boolean ageMatch = age == null || (user.getAge() != null && user.getAge().equals(age));
                boolean foodMatch = food == null || food.isEmpty() || 
                    user.getFavoriteFoods().stream()
                        .anyMatch(f -> f.getFoodName().contains(food));
                
                if ("OR".equalsIgnoreCase(searchType)) {
                    return nameMatch || genderMatch || ageMatch || foodMatch;
                } else {
                    return nameMatch && genderMatch && ageMatch && foodMatch;
                }
            })
            .map(user -> {
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("id", user.getId());
                userMap.put("username", user.getUsername());
                userMap.put("name", user.getName());
                userMap.put("gender", user.getGender());
                userMap.put("age", user.getAge());
                userMap.put("postalCode", user.getPostalCode());
                userMap.put("prefecture", user.getPrefecture());
                userMap.put("city", user.getCity());
                userMap.put("address", user.getAddress());
                userMap.put("phoneNumber", user.getPhoneNumber());
                userMap.put("nationality", user.getNationality());
                
                List<String> foods = user.getFavoriteFoods().stream()
                    .map(FavoriteFood::getFoodName)
                    .collect(Collectors.toList());
                userMap.put("favoriteFoods", foods);
                
                return userMap;
            })
            .collect(Collectors.toList());
    }
}
