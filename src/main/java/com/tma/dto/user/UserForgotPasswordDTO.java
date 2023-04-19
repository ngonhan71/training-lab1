package com.tma.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UserForgotPasswordDTO {

    @NotBlank
    @Email
    private String email;

    public UserForgotPasswordDTO() {}

    public UserForgotPasswordDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
