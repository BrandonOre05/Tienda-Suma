package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Proveedores;

import java.util.List;
import java.util.Optional;

public interface ProveedoresService {
    List<Proveedores> listarTodos();
    Optional<Proveedores> buscarPorId(Integer id);
    void guardar(Proveedores proveedor);
    void actualizar(Proveedores proveedor);
    void eliminar(Integer id);
}
