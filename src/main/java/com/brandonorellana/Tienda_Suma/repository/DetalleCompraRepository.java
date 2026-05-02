package com.brandonorellana.Tienda_Suma.repository;

import com.brandonorellana.Tienda_Suma.entity.DetalleCompra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleCompraRepository extends JpaRepository<DetalleCompra, Integer> {
    List<DetalleCompra> findByComprasIdCompra(Integer idCompra);
}
