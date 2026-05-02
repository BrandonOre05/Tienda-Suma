package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import com.brandonorellana.Tienda_Suma.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UsuariosServiceImpl implements UsuariosService {

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<Usuarios> listarTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuarios> buscarPorId(Integer id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Optional<Usuarios> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    @Override
    public Usuarios crear(Usuarios usuario) {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        usuario.setEstado(true);
        usuario.setFechaCreacion(LocalDateTime.now());
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuarios actualizar(Usuarios usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(Integer id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }
}