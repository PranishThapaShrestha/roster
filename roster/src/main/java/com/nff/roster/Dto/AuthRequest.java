package com.nff.roster.Dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String username;
    private String password;
}