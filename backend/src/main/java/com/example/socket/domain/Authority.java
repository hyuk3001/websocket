package com.example.socket.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@Table(name = "tb_authority")
public class Authority {

    @Id
    @Column(name = "authority_name", length = 50)
    private String authorityName;


    @Builder
    public Authority(String authorityName) {
        this.authorityName = authorityName;
    }
}
