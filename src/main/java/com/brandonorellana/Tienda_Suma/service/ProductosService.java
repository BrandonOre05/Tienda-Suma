package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Productos;

import java.util.List;
import java.util.Optional;

public interface ProductosService {
    List<Productos> listarTodos();
    Optional<Productos> buscarPorId(Integer id);
    void guardar(Productos producto);
    void actualizar(Productos producto);
    void eliminar(Integer id);
    boolean hayStockSuficiente(Integer id, int cantidad);
    void descontarStock(Integer id, int cantidad);
}
