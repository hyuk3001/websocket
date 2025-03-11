package com.example.socket.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class CustomAccount extends User {
    private Account account; // 이름, 나이, 전화번호
    public CustomAccount(Account account){
        super(account.getEmail() ,account.getPassword() , getAuthorities(account));
        this.account=account;
    }

    private static Collection<? extends GrantedAuthority> getAuthorities(Account account){
        // ROLE, Authority
        List<GrantedAuthority> grantedAuthorityList=new ArrayList<>();
        grantedAuthorityList.add(new SimpleGrantedAuthority(account.getRole()));

        for(Authority _auth :  account.getAuthorities()){
            grantedAuthorityList.add(new SimpleGrantedAuthority(_auth.getAuthorityName()));
        }
        return  grantedAuthorityList;
    }
}
