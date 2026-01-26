package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.FavoriteFood;
import com.example.demo.model.User;

@Repository
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
    
    // ユーザーに紐づく好きな食べ物を論理削除
    @Modifying
    @Query("UPDATE FavoriteFood f SET f.deletedFlag = true WHERE f.user = :user AND f.deletedFlag = false")
    void logicalDeleteByUser(@Param("user") User user);
}