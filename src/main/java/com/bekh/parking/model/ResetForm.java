package com.bekh.parking.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ResetForm {

    @NotBlank(message = "Please enter the code.")
    private String code;

    @NotBlank(message = "Password cannot be empty")
    private String newPassword;

    @NotBlank(message = "Confirm password cannot be empty")
    private String confirmNewPassword;
}