package com.brandonorellana.Tienda_Suma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Productos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @NotBlank(message = "El nombre del producto no puede ir vacío.")
    @Size(min = 2, max = 60, message = "El nombre debe tener entre 2 y 60 caracteres.")
    @Column(name = "nombre_producto", nullable = false, length = 60)
    private String nombreProducto;

    @Size(max = 65535, message = "La descripción es muy larga.")
    @Column(name = "descripcion")
    private String descripcion;

    @NotNull(message = "El precio no puede ir vacío.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0.")
    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @NotNull(message = "El stock no puede ir vacío.")
    @Min(value = 0, message = "El stock no puede ser negativo.")
    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Min(value = 0, message = "El stock mínimo no puede ser negativo.")
    @Column(name = "stock_minimo")
    private Integer stockMinimo = 5;

    @NotNull(message = "La categoría es obligatoria.")
    @Column(name = "categorias_id_categoria", nullable = false)
    private Integer categoriasIdCategoria;

    @NotNull(message = "El proveedor es obligatorio.")
    @Column(name = "proveedores_id_proveedor", nullable = false)
    private Integer proveedoresIdProveedor;

    @NotNull(message = "El estado no puede ir vacío.")
    @Column(name = "estado", nullable = false)
    private Boolean estado;
}