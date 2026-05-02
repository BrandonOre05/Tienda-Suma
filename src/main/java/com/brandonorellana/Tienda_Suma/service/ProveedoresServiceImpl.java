package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Proveedores;
import com.brandonorellana.Tienda_Suma.repository.ProveedoresRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProveedoresServiceImpl implements ProveedoresService {

    @Autowired
    private ProveedoresRepository proveedorRepository;

    @Override
    public List<Proveedores> listarTodos() {
        return proveedorRepository.findAll();
    }

    @Override
    public Optional<Proveedores> buscarPorId(Integer id) {
        return proveedorRepository.findById(id);
    }

    @Override
    public void guardar(Proveedores proveedor) {
        proveedorRepository.save(proveedor);
    }

    @Override
    public void actualizar(Proveedores proveedor) {
        proveedorRepository.save(proveedor);
    }

    @Override
    public void eliminar(Integer id) {
        proveedorRepository.deleteById(id);
    }
}