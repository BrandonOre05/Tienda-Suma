package com.brandonorellana.Tienda_Suma.repository;

import com.brandonorellana.Tienda_Suma.entity.Rol;
import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuariosRepository extends JpaRepository<Usuarios, Integer> {
    Optional<Usuarios> findByEmail(String email);
    List<Usuarios> findByRol(Rol rol);
    boolean existsByEmail(String email);
}