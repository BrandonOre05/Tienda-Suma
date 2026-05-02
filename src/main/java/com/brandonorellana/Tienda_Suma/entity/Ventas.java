package com.brandonorellana.Tienda_Suma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ventas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Integer idVenta;

    @Column(name = "fecha_venta", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaVenta;

    @NotNull(message = "El total no puede ir vacío.")
    @DecimalMin(value = "0.0", message = "El total no puede ser negativo.")
    @Column(name = "total", nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    @NotNull(message = "El cliente es obligatorio.")
    @Column(name = "usuarios_id_usuario", nullable = false)
    private Integer usuariosIdUsuario;

    @NotNull(message = "El vendedor es obligatorio.")
    @Column(name = "vendedor_id_usuario", nullable = false)
    private Integer vendedorIdUsuario;
}