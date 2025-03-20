package com.example.socket.DTO;

import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class TokenRequestDto {

    private String accessToken;
    private String refreshToken;

}