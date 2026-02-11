package com.example.demo.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;          // 追加/修正
import jakarta.validation.constraints.Min;          // 追加/修正
import jakarta.validation.constraints.NotBlank;     // 追加/修正
import jakarta.validation.constraints.NotNull;      // 追加/修正
import jakarta.validation.constraints.Pattern;      // 追加/修正
import jakarta.validation.constraints.Size;         // 追加/修正

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // ユーザー名: 英数字とアンダースコアのみ、3〜20文字
    @NotBlank(message = "ユーザー名は必須です") // 追加
    @Size(min = 3, max = 20, message = "ユーザー名は3〜20文字で入力してください") // 追加
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "ユーザー名は英数字とアンダースコアのみ使用可能です") // 追加
    @Column(nullable = false, unique = true)
    private String username;
    
    // パスワード: 最低6文字
    @NotBlank(message = "パスワードは必須です") // 追加
    @Size(min = 6, message = "パスワードは6文字以上で入力してください") // 追加
    @Column(nullable = false)
    private String password;
    
    // 氏名: 文字と空白のみ
    @NotBlank(message = "氏名は必須です") // 追加
    @Pattern(regexp = "^[\\p{L} 　]+$", message = "氏名は文字とスペースのみ使用可能です") // 追加
    @Column(nullable = false)
    private String name;
    
    // 性別: 男性/女性/その他のみ
    @NotBlank(message = "性別は必須です") // 追加
    @Pattern(regexp = "男性|女性|その他", message = "性別は「男性」「女性」「その他」から選択してください") // 追加
    private String gender;

    // 年齢: 0〜150
    @NotNull(message = "年齢は必須です") // 追加
    @Min(value = 0, message = "年齢は0以上で入力してください") // 追加
    @Max(value = 150, message = "年齢は150以下で入力してください") // 追加
    private Integer age;

    // 郵便番号: 7桁数字
    @NotBlank(message = "郵便番号は必須です") // 追加
    @Pattern(regexp = "^\\d{7}$", message = "郵便番号はハイフンなしの7桁で入力してください（例: 1000001）") // 追加
    private String postalCode;

    // 都道府県: 文字と空白のみ
    @NotBlank(message = "都道府県は必須です") // 追加
    @Pattern(regexp = "^[\\p{L} 　]+$", message = "都道府県は文字とスペースのみ使用可能です") // 追加
    private String prefecture;

    // 市区町村: 文字と空白のみ
    @NotBlank(message = "市区町村は必須です") // 追加
    @Pattern(regexp = "^[\\p{L} 　]+$", message = "市区町村は文字とスペースのみ使用可能です") // 追加
    private String city;

    // 住所: 柔軟に許可（数字・記号もOK）
    @NotBlank(message = "住所は必須です") // 追加
    private String address;

    // 電話番号: 10桁または11桁の数字
    @NotBlank(message = "電話番号は必須です") // 追加
    @Pattern(regexp = "^0\\d{9,10}$", message = "電話番号はハイフンなしの10桁または11桁で入力してください（例: 09012345678）") // 追加
    private String phoneNumber;
    private String nationality;
    
    @Column(name = "deleted_flag", nullable = false)
    private Boolean deletedFlag = false;
    
    // 監査フィールド
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
    
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;
    
    @LastModifiedBy
    @Column(name = "last_modified_by")
    private String lastModifiedBy;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FavoriteFood> favoriteFoods = new ArrayList<>();
    
    // デフォルトコンストラクタ
    public User() {}
    
    // Getter/Setter
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public Boolean getDeletedFlag() {
        return deletedFlag;
    }
    
    public void setDeletedFlag(Boolean deletedFlag) {
        this.deletedFlag = deletedFlag;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
    
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    
    public List<FavoriteFood> getFavoriteFoods() {
        return favoriteFoods;
    }
    
    public void setFavoriteFoods(List<FavoriteFood> favoriteFoods) {
        this.favoriteFoods = favoriteFoods;
    }
}