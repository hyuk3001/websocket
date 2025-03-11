package com.example.socket.controller;


import com.example.socket.DTO.AccountDTO;
import com.example.socket.domain.Account;
import com.example.socket.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<Account> signup(@RequestBody @Valid AccountDTO accountDTO) {
        return new ResponseEntity<>(userService.save(accountDTO), HttpStatus.OK);
    }

    @PostMapping("/user")
    public ResponseEntity<String> getMyUserInfo() {
        return ResponseEntity.ok("user");
    }

    @PostMapping("/user/{username}")
    public ResponseEntity<String> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok("admin");
    }

}