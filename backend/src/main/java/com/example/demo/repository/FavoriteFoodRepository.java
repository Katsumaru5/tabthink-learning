package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.FavoriteFood;

public interface FavoriteFoodRepository extends JpaRepository<FavoriteFood, Long> {
}