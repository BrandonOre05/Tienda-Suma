package com.brandonorellana.Tienda_Suma.repository;

import com.brandonorellana.Tienda_Suma.entity.Compras;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComprasRepository extends JpaRepository<Compras, Integer> {
    List<Compras> findByUsuariosIdUsuario(Integer idUsuario);
}