package com.example.demo.dto;

public class UserSearchDTO {
  private String username;
  private String name;
  private String gender;
  private Integer age;
  private String food;
  private String searchType;  // 追加（"AND" または "OR"）

  // デフォルトコンストラクタ
  public UserSearchDTO() {}

  // Getter & Setter
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
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

  // 追加: age
  public Integer getAge() {
    return age;
  }

  public void setAge(Integer age) {
    this.age = age;
  }

  // 追加: food
  public String getFood() {
    return food;
  }

  public void setFood(String food) {
    this.food = food;
  }

  // 追加: searchType
  public String getSearchType() {
    return searchType;
  }

  public void setSearchType(String searchType) {
    this.searchType = searchType;
  }

  @Override
  public String toString() {
    return "UserSearchDTO{" +
        "username='" + username + '\'' +
        ", name='" + name + '\'' +
        ", gender='" + gender + '\'' +
        ", age=" + age +
        ", food='" + food + '\'' +
        ", searchType='" + searchType + '\'' +
        '}';
  }
}