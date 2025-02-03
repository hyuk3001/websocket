package com.example.socket.service;

import com.example.socket.DTO.AccountDTO;
import com.example.socket.domain.Account;
import com.example.socket.domain.Authority;
import com.example.socket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Account signup(AccountDTO accountDTO) {

        if(userRepository.findOneWithAuthoritiesByEmail(accountDTO.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저 입니다.");
        }

        Authority authority = Authority.builder()
                .authorityName("USER")
                .build();

        Account user = Account.builder()
                .email(accountDTO.getEmail())
                .name(accountDTO.getName())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .phone(accountDTO.getPhone())
                .authorities(Collections.singleton(authority))
                .joinedAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

}