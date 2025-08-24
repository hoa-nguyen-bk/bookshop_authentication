package com.cybersoft.bookshop_authentication.dto;

import lombok.Data;

import java.util.List;

@Data
public class TokenDTO {
    private String token;
    private String email;
    private List<String> roles;
}
