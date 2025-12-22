package com.example.demo.user;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

//users DBのレコードをJavaのオブジェクトと対応させるクラス。DBとのデータ連携に必要。
@Entity	
@Table(name = "users")

public class User {
	@Id
	  @GeneratedValue(strategy = GenerationType.IDENTITY)
	  private Long id;
	//usersテーブルと対応させる。Userクラス外から変更されると困るので、private指定
	  private String username;
	  private String password;
	  
	  //⭐DBからUserオブジェクトを生成するために必要な引数なしコンストラクタ？　要確認
	  protected User() {}
	  
	  //usernameを直接変更できないように、getUsernameで取得する。
	  public String getUsername() { return username; }
	  public String getPassword() { return password; }

}	
