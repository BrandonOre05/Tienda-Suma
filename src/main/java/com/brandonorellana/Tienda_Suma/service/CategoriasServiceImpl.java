package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Categorias;
import com.brandonorellana.Tienda_Suma.repository.CategoriasRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriasServiceImpl implements CategoriasService {

    @Autowired
    private CategoriasRepository categoriaRepository;

    @Override
    public List<Categorias> listarTodos() {
        return categoriaRepository.findAll();
    }

    @Override
    public Optional<Categorias> buscarPorId(Integer id) {
        return categoriaRepository.findById(id);
    }

    @Override
    public void guardar(Categorias categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    public void actualizar(Categorias categoria) {
        categoriaRepository.save(categoria);
    }

    @Override
    public void eliminar(Integer id) {
        categoriaRepository.deleteById(id);
    }
}