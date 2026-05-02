package com.brandonorellana.Tienda_Suma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_venta")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_venta")
    private Integer idDetalleVenta;

    @NotNull(message = "La cantidad no puede ir vacía.")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0.")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @NotNull(message = "El precio unitario no puede ir vacío.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0.")
    @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioUnitario;

    @NotNull(message = "El producto es obligatorio.")
    @Column(name = "productos_id_producto", nullable = false)
    private Integer productosIdProducto;

    @NotNull(message = "La venta es obligatoria.")
    @Column(name = "ventas_id_venta", nullable = false)
    private Integer ventasIdVenta;
}
