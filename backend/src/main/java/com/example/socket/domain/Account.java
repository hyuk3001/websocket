package com.example.socket.domain;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "tb_account")
public class Account {

    @Id
    @JsonIgnore
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 회원 이메일(ID)
    @Column(name = "account_email", length = 50, unique = true)
    private String email;

    // 회원명
    @Column(name = "account_name", length = 50)
    private String name;

    // 비밀번호
    // 노출 방지를 위해 JsonIgnore 속성 부여
    @JsonIgnore
    @Column(name = "account_password", nullable = false, length = 100)
    private String password;

    @Column(name = "account_phone", length = 20, unique = true)
    private String phone;

    // 하나의 역할(Role)을 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private Role role;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "account_authority",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "authority_id")
    )
    private Set<Authority> authorities=new HashSet<>();

    @Builder
    public Account(String email, String name, String password, String phone, Role role, Set<Authority> authorities, LocalDateTime joinedAt) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.authorities = authorities != null ? Set.copyOf(authorities) : new HashSet<>();
        this.joinedAt = joinedAt;
    }

}
