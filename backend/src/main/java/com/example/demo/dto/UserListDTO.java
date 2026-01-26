package com.example.demo.dto;

import java.util.List;

/**
 * ユーザー一覧表示用の軽量DTO
 */
public class UserListDTO {
    private Long id;
    private String name;
    private String gender;
    private Integer age;
    private String postalCode;
    private String prefecture;
    private String city;
    private String address;
    private String phoneNumber;
    private String nationality;
    private List<String> favoriteFoods;
    
    // コンストラクタ（JPQLのコンストラクタ式用）
    public UserListDTO(Long id, String name, String gender, Integer age,
                       String postalCode, String prefecture, String city, String address,
                       String phoneNumber, String nationality) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.postalCode = postalCode;
        this.prefecture = prefecture;
        this.city = city;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
    }
    
    // デフォルトコンストラクタ
    public UserListDTO() {}
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getPostalCode() {
        return postalCode;
    }
    
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
    
    public String getPrefecture() {
        return prefecture;
    }
    
    public void setPrefecture(String prefecture) {
        this.prefecture = prefecture;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getNationality() {
        return nationality;
    }
    
    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
    
    public List<String> getFavoriteFoods() {
        return favoriteFoods;
    }
    
    public void setFavoriteFoods(List<String> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }
}