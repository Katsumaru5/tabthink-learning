package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.UserListDTO;
import com.example.demo.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 一覧表示用（DTO直接生成）
     */
    @Query("SELECT new com.example.demo.dto.UserListDTO(" +
           "u.id, u.name, u.gender, u.age, u.postalCode, " +
           "u.prefecture, u.city, u.address, u.phoneNumber, u.nationality) " +
           "FROM User u " +
           "WHERE u.deletedFlag = false")
    List<UserListDTO> findAllUsersForList();


    /**
     * 全アクティブユーザー取得（OR検索用）
     */
    @Query("SELECT u FROM User u " +
           "WHERE u.deletedFlag = false")
    List<User> findAllActiveUsersWithFoods();


    /**
     * ユーザー名検索（ログイン用）
     */
    Optional<User> findByUsernameAndDeletedFlag(String username, Boolean deletedFlag);


    /**
     * AND検索（食べ物なし）
     */
    @Query("SELECT u FROM User u " +
           "WHERE u.deletedFlag = false " +
           "AND (:name IS NULL OR u.name LIKE %:name%) " +
           "AND (:gender IS NULL OR u.gender = :gender) " +
           "AND (:age IS NULL OR u.age = :age)")
    List<User> searchUsersAnd(
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("age") Integer age
    );


    /**
     * AND検索（食べ物あり）
     */
    @Query("SELECT u FROM User u, FavoriteFood f " +
           "WHERE u.id = f.user.id " +
           "AND u.deletedFlag = false " +
           "AND f.deletedFlag = false " +
           "AND (:name IS NULL OR u.name LIKE %:name%) " +
           "AND (:gender IS NULL OR u.gender = :gender) " +
           "AND (:age IS NULL OR u.age = :age) " +
           "AND f.foodName LIKE %:food%")
    List<User> searchUsersAndWithFood(
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("age") Integer age,
        @Param("food") String food
    );


    /**
     * ユーザーIDリストから好きな食べ物取得
     */
    @Query("SELECT f.user.id, f.foodName FROM FavoriteFood f " +
           "WHERE f.user.id IN :userIds " +
           "AND f.deletedFlag = false")
    List<Object[]> findFavoriteFoodsByUserIds(
        @Param("userIds") List<Long> userIds
    );
}
