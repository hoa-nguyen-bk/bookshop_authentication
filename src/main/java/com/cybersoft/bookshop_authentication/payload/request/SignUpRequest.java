package com.cybersoft.bookshop_authentication.payload.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SignUpRequest {
    @Email(message = "Invalid email format")
    private String email;
    private String fullName;

    @NotNull(message = "Phone number cannot be null")
    private String phoneNumber;
    private String address;
}
