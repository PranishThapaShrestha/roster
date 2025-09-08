package com.nff.roster.Dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String role; // "EMPLOYEE" or "SUPERVISOR" or "ADMIN"
}
