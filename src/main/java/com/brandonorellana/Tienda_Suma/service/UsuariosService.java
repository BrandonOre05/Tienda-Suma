package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Usuarios;

import java.util.List;
import java.util.Optional;

public interface UsuariosService {
    List<Usuarios> listarTodos();
    Optional<Usuarios> buscarPorId(Integer id);
    Optional<Usuarios> buscarPorEmail(String email);
    Usuarios crear(Usuarios usuario);
    Usuarios actualizar(Usuarios usuario);
    void eliminar(Integer id);
    boolean existsByEmail(String email);
}