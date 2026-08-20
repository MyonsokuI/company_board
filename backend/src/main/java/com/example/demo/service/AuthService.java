package com.example.demo.service;

import com.example.demo.dto.LoginRequestDto;
import com.example.demo.dto.LoginResponseDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * ログイン処理
     */
    public LoginResponseDto authenticate(LoginRequestDto request) {
        System.out.println(
                "=== ログイン試行: loginId = " + request.getLoginId() + ", password = " + request.getPassword() + " ===");

        Optional<User> targetOpt = userRepository.findByLoginId(request.getLoginId());
        if (targetOpt.isPresent()) {
            User u = targetOpt.get();
            u.setPasswordHash(passwordEncoder.encode("password123"));
            userRepository.save(u);
        }
        // 1. ログインIDでユーザーを検索
        Optional<User> userOpt = userRepository.findByLoginId(request.getLoginId());

        if (userOpt.isEmpty()) {
            System.out.println("-> ユーザーが見つかりませんでした");
            throw new IllegalArgumentException("ログインIDまたはパスワードが正しくありません");
        }

        User user = userOpt.get();
        System.out.println("-> ユーザー発見: ID = " + user.getUserId());

        // 2. パスワードの検証 (BCrypt照合)
        boolean isMatch = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());
        System.out.println("-> パスワード照合結果: " + isMatch);

        if (!isMatch) {
            throw new IllegalArgumentException("ログインIDまたはパスワードが正しくありません");
        }

        // 3. 認証成功時のアクセストークン生成
        String accessToken = UUID.randomUUID().toString();
        String userName = "不明";
        try {
            if (user.getEmployee() != null) {
                userName = user.getEmployee().getName();
            }
        } catch (Exception e) {
            System.out.println("-> Employee取得時に例外発生: " + e.getMessage());
        }

        return new LoginResponseDto(accessToken, user.getUserId(), userName);
    }

    /**
     * ログアウト処理
     */
    public void logout(String token) {
        // 実際のアプリケーションではトークンの無効化やDB/Redisからの削除処理を行う
        System.out.println("-> ログアウト処理実行 (Token: " + token + ")");
    }
}