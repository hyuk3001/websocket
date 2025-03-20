package com.example.socket.service;

import lombok.RequiredArgsConstructor;
import com.example.socket.jwt.TokenProvider;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String PREFIX = "refreshToken:"; // Redis Key Prefix

    /**
     * RefreshToken 저장
     */
    public void saveToken(String email, String refreshToken) {
        String key = PREFIX + email;
//        long expiration = System.currentTimeMillis() + TokenProvider.REFRESH_TOKEN_EXPIRE_TIME;
        System.out.println("key : "+ key);
        System.out.println("refreshToken : "+ refreshToken);
        System.out.println("TokenProvider.REFRESH_TOKEN_EXPIRE_TIME : "+ TokenProvider.REFRESH_TOKEN_EXPIRE_TIME);
        System.out.println("TimeUnit.MILLISECONDS : "+ TimeUnit.MILLISECONDS);
        redisTemplate.opsForValue().set(key, refreshToken, TokenProvider.REFRESH_TOKEN_EXPIRE_TIME, TimeUnit.MILLISECONDS);
    }

    /**
     * RefreshToken 조회
     */
    public String getToken(String email) {
        String key = PREFIX + email;
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * RefreshToken 삭제
     */
    public void deleteToken(String email) {
        String key = PREFIX + email;
        redisTemplate.delete(key);
    }
}