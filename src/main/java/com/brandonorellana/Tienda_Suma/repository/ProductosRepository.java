package com.brandonorellana.Tienda_Suma.repository;

import com.brandonorellana.Tienda_Suma.entity.Productos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductosRepository extends JpaRepository<Productos, Integer> {
}
