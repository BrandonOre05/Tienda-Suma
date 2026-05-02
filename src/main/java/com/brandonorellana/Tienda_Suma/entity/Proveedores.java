package com.brandonorellana.Tienda_Suma.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "proveedores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proveedores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor")
    private Integer idProveedor;

    @NotBlank(message = "El nombre del proveedor no puede ir vacío.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    @Column(name = "nombre_proveedor", nullable = false, length = 100)
    private String nombreProveedor;

    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe contener 8 números.")
    @Column(name = "telefono", length = 15)
    private String telefono;

    @Email(message = "El email debe tener un formato válido.")
    @Column(name = "email", length = 60)
    private String email;

    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres.")
    @Column(name = "direccion", length = 100)
    private String direccion;

    @NotNull(message = "El estado no puede ir vacío.")
    @Column(name = "estado", nullable = false)
    private Boolean estado;
}