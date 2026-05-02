package com.brandonorellana.Tienda_Suma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "compras")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Compras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_compra")
    private Integer idCompra;

    @Column(name = "fecha_compra", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCompra;

    @DecimalMin(value = "0.0", message = "El total no puede ser negativo.")
    @Column(name = "total")
    private BigDecimal total = BigDecimal.ZERO;

    @Size(max = 50, message = "El número de factura no puede exceder los 50 caracteres.")
    @Column(name = "factura_numero", length = 50)
    private String facturaNumero;

    @NotNull(message = "El proveedor es obligatorio.")
    @Column(name = "proveedores_id_proveedor", nullable = false)
    private Integer proveedoresIdProveedor;

    @NotNull(message = "El usuario es obligatorio.")
    @Column(name = "usuarios_id_usuario", nullable = false)
    private Integer usuariosIdUsuario;
}
