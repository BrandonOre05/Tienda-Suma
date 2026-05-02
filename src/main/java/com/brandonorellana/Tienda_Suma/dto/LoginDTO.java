package com.brandonorellana.Tienda_Suma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginDTO {
    @NotBlank(message = "El email no puede ir vacio.")
    @Email(message = "Ingrese un email válido")
    private String email;

    @NotBlank(message = "La contraseña no puede ir vacia.")
    private String password;
}