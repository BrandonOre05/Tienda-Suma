package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface DetalleVentaService {
    List<DetalleVenta> listarTodos();
    Optional<DetalleVenta> buscarPorId(Integer id);
    List<DetalleVenta> buscarPorVentaId(Integer idVenta);
    void guardar(DetalleVenta detalleVenta);
    void eliminar(Integer id);
}
