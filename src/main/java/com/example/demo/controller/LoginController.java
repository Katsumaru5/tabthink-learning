package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;

@Controller
// ブラウザから送られるユーザ名、パスワード、ログアウト操作を受け取り、操作内容に応じてログイン後画面、エラー画面、ログイン画面を返すクラス
public class LoginController {
	
	//private final であとで変更できなくする。
  private final UserRepository userRepository;
  
  public LoginController(UserRepository userRepository) {
    this.userRepository = userRepository;
  }
  
  //入力されたユーザ名、パスワードを受け取る
  @PostMapping("/login")
  public String login(
      @RequestParam String username,
      @RequestParam String password,
      RedirectAttributes redirectAttributes
  ) 
  
  {
	  	//DBからusernameが一致するユーザを探す。見つからなければnull。Userオブジェクトに変換
    User user = userRepository.findByUsername(username).orElse(null);
    	//ユーザが存在しないまたはパスワードが一致しない場合にエラー画面を返す
    if (user == null || !user.getPassword().equals(password)) {
        return "redirect:/login.html?error=true";
      }
    	//パスワードが一致する場合にログイン後画面を返す
    return "redirect:/index.html";
  }
  @PostMapping("/logout")
  public String logout(HttpSession session) {
    //セッションを切り、ログイン状態をリセット
	session.invalidate();
    return "redirect:/login.html";
  }
}