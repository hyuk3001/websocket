package com.example.socket.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
public class JoinRequestDto {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String phone;
    @Nullable
    private String role;
}
