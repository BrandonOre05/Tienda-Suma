package com.brandonorellana.Tienda_Suma.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterVendedorDTO {
    @NotBlank(message = "El nombre no puede ir vacío.")
    @Size(min = 2, max = 60)
    private String nombre;

    @NotBlank(message = "El apellido no puede ir vacío.")
    @Size(min = 2, max = 60)
    private String apellido;

    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe contener 8 números.")
    private String telefono;

    private String direccion;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email
    private String email;

    @NotBlank(message = "La contraseña no puede ir vacía.")
    @Size(min = 8, max = 60)
    private String password;

    @NotBlank(message = "Confirmar contraseña es obligatorio.")
    private String confirmPassword;
}