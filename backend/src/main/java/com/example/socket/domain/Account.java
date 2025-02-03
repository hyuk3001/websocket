package com.example.socket.domain;


import java.time.LocalDateTime;
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

    @Column(name = "account_email", length = 50, unique = true)
    private String email;

    @Column(name = "account_name", length = 50)
    private String name;

    @JsonIgnore
    @Column(name = "account_password", nullable = false, length = 100)
    private String password;

    @Column(name = "account_phone", length = 20, unique = true)
    private String phone;

    @CreationTimestamp
    @Column(name = "joined_at", updatable = false)
    private LocalDateTime joinedAt = LocalDateTime.now();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "account_authority",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id")}, // 수정
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")}
    )
    private Set<Authority> authorities;

    @Builder
    public Account(String email, String password, String name, String phone, Set<Authority> authorities, LocalDateTime joinedAt) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phone = phone;
        this.authorities = authorities;
        this.joinedAt = joinedAt;
    }

}
