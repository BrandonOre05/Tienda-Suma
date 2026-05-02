package com.brandonorellana.Tienda_Suma.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorias {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @NotBlank(message = "El nombre de la categoría no puede ir vacío.")
    @Size(min = 2, max = 50, message = "El nombre debe tener entre 2 y 50 caracteres.")
    @Column(name = "nombre_categoria", nullable = false, unique = true, length = 50)
    private String nombreCategoria;

    @Size(max = 65535, message = "La descripción es muy larga.")
    @Column(name = "descripcion")
    private String descripcion;
}