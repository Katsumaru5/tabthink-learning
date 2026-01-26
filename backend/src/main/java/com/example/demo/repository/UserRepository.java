package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameAndDeletedFlag(String username, boolean deletedFlag);
    
    List<User> findByDeletedFlag(boolean deletedFlag);
    //⭐️取得するデータを絞る条件を追記する.DISTINCTは不要。結合条件「ON・・・」で条件設定する。WHERE句でANDとOR両方について切り替えができるように検索条件を設定する(4つ)
    @Query("SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.favoriteFoods WHERE u.deletedFlag = false")
    List<User> findAllWithFavoriteFoods();
}