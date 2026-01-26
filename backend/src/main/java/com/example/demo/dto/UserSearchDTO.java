package com.example.demo.dto;

public class UserSearchDTO {
  private String username;
  private String name;
  private String gender;
  private Integer ageFrom;
  private Integer ageTo;
  private Integer age;  // 追加
  private String food;  // 追加
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

  public Integer getAgeFrom() {
    return ageFrom;
  }

  public void setAgeFrom(Integer ageFrom) {
    this.ageFrom = ageFrom;
  }

  public Integer getAgeTo() {
    return ageTo;
  }

  public void setAgeTo(Integer ageTo) {
    this.ageTo = ageTo;
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
        ", ageFrom=" + ageFrom +
        ", ageTo=" + ageTo +
        ", age=" + age +
        ", food='" + food + '\'' +
        ", searchType='" + searchType + '\'' +
        '}';
  }
}