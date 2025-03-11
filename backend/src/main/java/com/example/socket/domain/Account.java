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

    @Column(name = "account_role", length = 20)
    private String role = "USER";

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_authority",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id")}, // 수정
            inverseJoinColumns = {@JoinColumn(name = "authority_id", referencedColumnName = "authority_id")}
    )
    private Set<Authority> authorities=new HashSet<>();

    @Builder
    public Account(String email, String password, String name, String phone, String role, Set<Authority> authorities, LocalDateTime joinedAt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.authorities = authorities;
        this.joinedAt = joinedAt;
    }

}
