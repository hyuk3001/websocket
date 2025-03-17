package com.example.socket.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_authority")
public class Authority {

    @Id
    @JsonIgnore
    @Column(name = "authority_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorityId;

    @Column(name = "authority_name", length = 50)
    private String authorityName;

    // 1. USER : 일반회원, 2. SELLER : 판매자, 3. ADMIN : 관리자
    @Builder
    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }

}
