package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    @Transactional
    public Map<String, Object> register(User user, List<String> foodNames) {
        Map<String, Object> response = new HashMap<>();
        
        if (userRepository.findByUsernameAndDeletedFlag(user.getUsername(), false).isPresent()) {
            response.put("success", false);
            response.put("error", "このユーザー名は既に使用されています");
            return response;
        }
        
        User savedUser = userRepository.save(user);
        
        if (foodNames != null && !foodNames.isEmpty()) {
            for (String foodName : foodNames) {
                FavoriteFood food = new FavoriteFood();
                food.setUser(savedUser);
                food.setFoodName(foodName);
                favoriteFoodRepository.save(food);
            }
        }
        
        response.put("success", true);
        response.put("message", "登録成功");
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
