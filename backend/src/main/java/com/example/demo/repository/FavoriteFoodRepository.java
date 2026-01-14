package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.FavoriteFood;

//JpaRepositoryの詳細を見てみる.
public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
}