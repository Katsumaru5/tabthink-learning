package com.example.demo.config;

import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//データベースの変更履歴を自動記録する仕組みを設定する

@Configuration//このクラスは設定ファイルです」とSpringに伝えるアノテーション
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")//JPA監査機能を有効化するためのアノテーション
public class JpaAuditingConfig {
    @Bean//このメソッドが返すオブジェクトをSpringに管理させる」という指示を出すアノテーション
    public AuditorAware<String> auditorProvider() {
        return new AuditorAware<String>() {
            @Override
            public Optional<String> getCurrentAuditor() {
                return Optional.of("system");
            }
        };
    }
}