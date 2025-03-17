package com.example.socket.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

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
