package com.tma.dto.user;

import javax.validation.constraints.NotBlank;

public class UserResetPasswordDTO {

    @NotBlank
    private String resetPasswordCode;

    @NotBlank
    private String password;

    public UserResetPasswordDTO() {}

    public UserResetPasswordDTO(String resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public String getResetPasswordCode() {
        return resetPasswordCode;
    }

    public void setResetPasswordCode(String resetPasswordCode) {
        this.resetPasswordCode = resetPasswordCode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
