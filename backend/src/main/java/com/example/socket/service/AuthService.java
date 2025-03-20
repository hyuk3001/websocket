package com.example.socket.service;

import com.example.socket.DTO.*;
import lombok.RequiredArgsConstructor;
import com.example.socket.domain.Role;
import com.example.socket.domain.Account;
import com.example.socket.jwt.TokenProvider;
import com.example.socket.domain.RefreshToken;
import org.springframework.stereotype.Service;
import com.example.socket.repository.RoleRepository;
import com.example.socket.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenService refreshTokenService;

    @Transactional
    public UserResponseDto<?> signup(JoinRequestDto joinRequestDto) {
        if(userRepository.findOneWithAuthoritiesByEmail(joinRequestDto.getEmail()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저 입니다.");
        }

        Role role;
        if (joinRequestDto.getRole() == null || joinRequestDto.getRole().equalsIgnoreCase("user")) {
            // Role 테이블에서 기본 "user" 역할 가져오기
            role = roleRepository.findByRoleName("user")
                    .orElseThrow(() -> new RuntimeException("기본 'user' 역할이 데이터베이스에 등록되어 있지 않습니다."));
        } else {
            // 요청에 명시된 역할 유효성 검증
            role = roleRepository.findByRoleName(joinRequestDto.getRole())
                    .orElseThrow(() -> new RuntimeException("유효하지 않은 역할입니다: " + joinRequestDto.getRole()));
        }

        Account user = Account.builder()
                .email(joinRequestDto.getEmail())
                .password(passwordEncoder.encode(joinRequestDto.getPassword()))
                .name(joinRequestDto.getName())
                .phone(joinRequestDto.getPhone())
                .role(role)
                .joinedAt(LocalDateTime.now())
                .build();

        // 데이터베이스에 유저 저장 시도
        try {
            userRepository.save(user);
        } catch (Exception e) {
            logger.severe("회원 저장 실패: " + e.getMessage());
            return UserResponseDto.setFailed("회원가입 중 문제가 발생했습니다.");

        }

        // 회원가입 성공 메시지 구체화
        return UserResponseDto.setSuccess("회원가입이 완료되었습니다. 로그인 해주세요.");
    }

    public Optional<Account> findByEmail(String email) {return userRepository.findOneByEmailIgnoreCase(email);}

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {
        // 1. Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        // 2. 실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        //    authenticate 메서드가 실행이 될 때 CustomUserDetailsService 에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        System.out.println("AuthService1");
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 4. RefreshToken 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        System.out.println("AuthService2");
        // 4. RefreshToken을 Redis에 저장
        refreshTokenService.saveToken(
                authentication.getName(),  // userId 역할
                tokenDto.getRefreshToken()
        );

        System.out.println("AuthService3");
        // 5. 토큰 발급
        return tokenDto;
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1. Refresh Token 검증
        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2. Access Token 에서 Member ID 가져오기
        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        // 3. Redis에서 Refresh Token 조회
        String storedRefreshToken = refreshTokenService.getToken(authentication.getName());
        if (storedRefreshToken == null) {
            throw new RuntimeException("로그아웃 상태입니다. Refresh Token이 존재하지 않습니다.");
        }

        // 4. Refresh Token 값이 요청의 그것과 일치하는지 확인
        if (!storedRefreshToken.equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token이 일치하지 않습니다.");
        }

        // 5. 새로운 Access Token & Refresh Token 발행
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        // 6. Refresh Token 갱신 (만료 기간 7일 연장)
        refreshTokenService.saveToken(
                authentication.getName(),
                tokenDto.getRefreshToken()
        );

        // 토큰 발급
        return tokenDto;
    }

    @Transactional
    public void logout(String email) {
        // 로그아웃 시 Refresh Token 삭제
        refreshTokenService.deleteToken(email);
    }

}