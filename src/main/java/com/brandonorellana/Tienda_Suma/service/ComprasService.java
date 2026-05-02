package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Compras;
import com.brandonorellana.Tienda_Suma.entity.DetalleCompra;

import java.util.List;
import java.util.Optional;

public interface ComprasService {
    List<Compras> listarTodos();
    Optional<Compras> buscarPorId(Integer id);
    List<Compras> buscarPorUsuario(Integer idUsuario);
    List<DetalleCompra> obtenerDetalles(Integer idCompra);
    void guardar(Compras compra, List<DetalleCompra> detalles);
    void eliminar(Integer id);
}