package com.example.socket.service;

import com.example.socket.domain.Account;
import com.example.socket.jwt.TokenProvider;
import com.example.socket.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger logger = (Logger) LoggerFactory.getLogger(TokenProvider.class);
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account userEntity = userRepository.findOneWithAuthoritiesByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("유저정보가 없습니다."));

        // 로그 추가
        logger.debug("사용자 정보 조회: {}", userEntity);  // DB에서 가져온 userEntity 확인

        List<GrantedAuthority> grantedAuthorities = userEntity.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthorityName()))
                .collect(Collectors.toList());

        return User.builder()
                .username(userEntity.getEmail())
                .password(userEntity.getPassword())
                .authorities(grantedAuthorities)
                .build();
    }
}