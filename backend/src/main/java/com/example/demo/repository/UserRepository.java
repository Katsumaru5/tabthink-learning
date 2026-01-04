package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndDeletedFlag(String username, boolean deletedFlag);
    
    List<User> findByDeletedFlag(boolean deletedFlag);
    
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.favoriteFoods WHERE u.deletedFlag = false")
    List<User> findAllWithFavoriteFoods();
}