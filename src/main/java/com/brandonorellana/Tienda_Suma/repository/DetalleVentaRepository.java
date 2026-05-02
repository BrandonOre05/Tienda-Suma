package com.brandonorellana.Tienda_Suma.repository;

import com.brandonorellana.Tienda_Suma.entity.DetalleVenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleVentaRepository extends JpaRepository<DetalleVenta, Integer> {
    List<DetalleVenta> findByVentasIdVenta(Integer idVenta);
}
