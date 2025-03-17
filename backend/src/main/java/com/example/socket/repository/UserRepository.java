package com.example.socket.repository;

import com.example.socket.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<Account, Long> {
    Optional<Account> findOneWithAuthoritiesByEmail(String email);
    Optional<Account> findOneByEmailIgnoreCase(String email);
}