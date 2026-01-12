package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity//このクラスはデータベースに保存することを宣言
@Table(name = "favorite_foods")//テーブル「favorite_foods」と対応させることを宣言
public class FavoriteFood {
    
    @Id//主キー
    @GeneratedValue(strategy = GenerationType.IDENTITY)//テーブル側でidを自動採番
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)//多対一であることを宣言。user情報が必要になるまで読み込まないパフォーマンス対策
    @JoinColumn(name = "user_id", nullable = false)//avorite_foods テーブルの user_id カラムでUserのidと対応づける
    @JsonIgnore
    private User user;
    
    @Column(name = "food_name", nullable = false)
    private String foodName;
    
    public FavoriteFood() {}//空の状態で箱だけ用意
    
    public FavoriteFood(User user, String foodName) {
        this.user = user;
        this.foodName = foodName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public String getFoodName() {
        return foodName;
    }
    
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
}