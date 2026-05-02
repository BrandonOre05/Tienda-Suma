package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.DetalleCompra;

import java.util.List;
import java.util.Optional;

public interface DetalleCompraService {
    List<DetalleCompra> listarTodos();
    Optional<DetalleCompra> buscarPorId(Integer id);
    List<DetalleCompra> buscarPorCompraId(Integer idCompra);
    void guardar(DetalleCompra detalleCompra);
    void eliminar(Integer id);
}
