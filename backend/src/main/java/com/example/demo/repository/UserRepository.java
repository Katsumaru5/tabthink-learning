package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsernameAndDeletedFlag(String username, Boolean deletedFlag);
    
    // 全ユーザ取得（論理削除されていないもののみ）
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.favoriteFoods f " +
           "WHERE u.deletedFlag = false " +
           "AND (f IS NULL OR f.deletedFlag = false)")
    List<User> findAllActiveUsersWithFoods();
    
    // AND検索用（名前・性別・年齢）
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN FETCH u.favoriteFoods f " +
           "WHERE u.deletedFlag = false " +
           "AND (f IS NULL OR f.deletedFlag = false) " +
           "AND (:name IS NULL OR :name = '' OR u.name LIKE %:name%) " +
           "AND (:gender IS NULL OR :gender = '' OR u.gender = :gender) " +
           "AND (:age IS NULL OR u.age = :age)")
    List<User> searchUsersAnd(
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("age") Integer age
    );
    
    // AND検索用（食べ物を含む）
    @Query("SELECT DISTINCT u FROM User u " +
           "LEFT JOIN u.favoriteFoods f " +
           "WHERE u.deletedFlag = false " +
           "AND (:name IS NULL OR :name = '' OR u.name LIKE %:name%) " +
           "AND (:gender IS NULL OR :gender = '' OR u.gender = :gender) " +
           "AND (:age IS NULL OR u.age = :age) " +
           "AND (:food IS NULL OR :food = '' OR EXISTS (" +
           "    SELECT ff FROM FavoriteFood ff " +
           "    WHERE ff.user = u " +
           "    AND ff.deletedFlag = false " +
           "    AND ff.foodName LIKE %:food%" +
           "))")
    List<User> searchUsersAndWithFood(
        @Param("name") String name,
        @Param("gender") String gender,
        @Param("age") Integer age,
        @Param("food") String food
    );
}