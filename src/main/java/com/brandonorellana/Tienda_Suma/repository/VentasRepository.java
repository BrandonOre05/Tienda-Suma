package com.brandonorellana.Tienda_Suma.repository;

import com.brandonorellana.Tienda_Suma.entity.Ventas;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VentasRepository extends JpaRepository<Ventas, Integer> {
    List<Ventas> findByUsuariosIdUsuario(Integer idUsuario);
    List<Ventas> findByVendedorIdUsuario(Integer idVendedor);
}
