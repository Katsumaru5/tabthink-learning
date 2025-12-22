package com.example.demo.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
//UserをDBで扱う機能（保存、IDで取得、全件取得、削除）を全てUserRepositoryがもらう。usernameで検索する機能を固有機能とする。
public interface UserRepository extends JpaRepository<User, Long> {
	  Optional<User> findByUsername(String username);
	}
