package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Categorias;

import java.util.List;
import java.util.Optional;

public interface CategoriasService {
    List<Categorias> listarTodos();
    Optional<Categorias> buscarPorId(Integer id);
    void guardar(Categorias categoria);
    void actualizar(Categorias categoria);
    void eliminar(Integer id);
}
