package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.UserListDTO;
import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    
    // 一覧表示用: 必要なフィールドのみ取得
    @Query("SELECT new com.example.demo.dto.UserListDTO(" +
           "u.id, u.name, u.gender, u.age, u.postalCode, " +
           "u.prefecture, u.city, u.address, u.phoneNumber, u.nationality) " +
           "FROM User u " +
           "WHERE u.deletedFlag = false")
    List<UserListDTO> findAllUsersForList();
    
    // 詳細取得用
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.favoriteFoods f " +
           "WHERE u.deletedFlag = false " +
           "AND (f.id IS NULL OR f.deletedFlag = false)")
    List<User> findAllActiveUsersWithFoods();
    
    // ユーザー名で検索
    Optional<User> findByUsernameAndDeletedFlag(String username, Boolean deletedFlag);
    
    // AND検索（食べ物なし）
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.favoriteFoods f " +
           "WHERE u.deletedFlag = false " +
           "AND (:name IS NULL OR u.name LIKE %:name%) " +
           "AND (:gender IS NULL OR u.gender = :gender) " +
           "AND (:age IS NULL OR u.age = :age) " +
           "AND (f.id IS NULL OR f.deletedFlag = false)")
    List<User> searchUsersAnd(
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("age") Integer age
    );
    
    // AND検索（食べ物あり）
    @Query("SELECT DISTINCT u FROM User u " +
           "INNER JOIN u.favoriteFoods f " +
           "WHERE u.deletedFlag = false " +
           "AND (:name IS NULL OR u.name LIKE %:name%) " +
           "AND (:gender IS NULL OR u.gender = :gender) " +
           "AND (:age IS NULL OR u.age = :age) " +
           "AND f.deletedFlag = false " +
           "AND f.foodName LIKE %:food%")
    List<User> searchUsersAndWithFood(
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("age") Integer age,
        @Param("food") String food
    );
    
    // ユーザーIDリストから好きな食べ物を取得
    @Query("SELECT f.user.id, f.foodName FROM FavoriteFood f " +
           "WHERE f.user.id IN :userIds AND f.deletedFlag = false")
    List<Object[]> findFavoriteFoodsByUserIds(@Param("userIds") List<Long> userIds);
}