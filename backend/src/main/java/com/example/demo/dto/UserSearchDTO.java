package com.example.demo.dto;

import jakarta.validation.constraints.Pattern;

public class UserSearchDTO {
    
    private String name;
    private String gender;
    private Integer age;
    private String food;
    
    @Pattern(regexp = "AND|OR", message = "検索タイプは「AND」または「OR」を指定してください")
    private String searchType = "AND";
    
    // デフォルトコンストラクタ
    public UserSearchDTO() {}
    
    // Getter/Setter
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public Integer getAge() {
        return age;
    }
    
    public void setAge(Integer age) {
        this.age = age;
    }
    
    public String getFood() {
        return food;
    }
    
    public void setFood(String food) {
        this.food = food;
    }
    
    public String getSearchType() {
        return searchType;
    }
    
    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }
}