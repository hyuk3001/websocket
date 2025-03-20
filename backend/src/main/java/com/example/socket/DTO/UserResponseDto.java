package com.example.socket.DTO;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor(staticName = "set")
public class UserResponseDto<D> {

    private boolean result;
    private String message;
    private D data;

    public  static <D> UserResponseDto<D> setSuccess(String message) {
        return UserResponseDto.set(true, message, null);
    }

    public static <D> UserResponseDto<D> setFailed(String message)
    {
        return UserResponseDto.set(false, message, null);
    }
}
