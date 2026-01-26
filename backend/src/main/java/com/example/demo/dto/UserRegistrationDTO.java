package com.example.demo.dto;

import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationDTO {

  @NotBlank(message = "ユーザー名は必須です")
  @Size(min = 3, max = 20, message = "ユーザー名は3〜20文字で入力してください")
  private String username;

  @NotBlank(message = "パスワードは必須です")
  @Size(min = 6, message = "パスワードは6文字以上で入力してください")
  private String password;

  @NotBlank(message = "氏名は必須です")
  private String name;

  @NotBlank(message = "性別は必須です")
  @Pattern(regexp = "男性?|女性?|その他", message = "性別は「男性」「女性」「その他」から選択してください")
  private String gender;

  @NotNull(message = "年齢は必須です")
  @Min(value = 0, message = "年齢は0以上で入力してください")
  @Max(value = 150, message = "年齢は150以下で入力してください")
  private Integer age;

  @NotBlank(message = "郵便番号は必須です")
  @Pattern(regexp = "^\\d{7}$", message = "郵便番号はハイフンなしの7桁で入力してください（例: 1000001）")
  private String postalCode;

  @NotBlank(message = "都道府県は必須です")
  private String prefecture;

  @NotBlank(message = "市区町村は必須です")
  private String city;

  @NotBlank(message = "住所は必須です")
  private String address;

  @NotBlank(message = "電話番号は必須です")
  @Pattern(regexp = "^0\\d{9,10}$", message = "電話番号はハイフンなしの10桁または11桁で入力してください（例: 09012345678）")
  private String phoneNumber;

  @NotBlank(message = "国籍は必須です")
  private String nationality;

  private List<String> favoriteFoods;

  // デフォルトコンストラクタ
  public UserRegistrationDTO() {}

  // Getter & Setter
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
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

  @Override
  public String toString() {
    return "UserRegistrationDTO{" +
        "username='" + username + '\'' +
        ", name='" + name + '\'' +
        ", gender='" + gender + '\'' +
        ", age=" + age +
        ", postalCode='" + postalCode + '\'' +
        ", prefecture='" + prefecture + '\'' +
        ", city='" + city + '\'' +
        ", nationality='" + nationality + '\'' +
        '}';
  }
}