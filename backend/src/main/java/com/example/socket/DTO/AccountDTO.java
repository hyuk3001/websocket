package com.example.socket.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AccountDTO {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
}
