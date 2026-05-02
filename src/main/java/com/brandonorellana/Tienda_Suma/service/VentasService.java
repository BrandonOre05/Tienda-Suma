package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.DetalleVenta;
import com.brandonorellana.Tienda_Suma.entity.Ventas;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface VentasService {
    List<Ventas> listarTodos();
    Optional<Ventas> buscarPorId(Integer id);
    List<Ventas> buscarPorCliente(Integer idCliente);
    void registrarVenta(Ventas venta, List<DetalleVenta> detalles);
    BigDecimal calcularTotalVenta(Integer idVenta);
    void eliminar(Integer id);
}