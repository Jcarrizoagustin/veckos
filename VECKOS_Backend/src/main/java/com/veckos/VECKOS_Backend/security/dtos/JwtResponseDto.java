package com.veckos.VECKOS_Backend.security.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
class JwtResponseDto {

    private String token;
    private String type = "Bearer";
    private String username;
    private Set<String> roles;

    public JwtResponseDto(String token, String username, Set<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }
}
