package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.DetalleVenta;
import com.brandonorellana.Tienda_Suma.repository.DetalleVentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleVentaServiceImpl implements DetalleVentaService {

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
    }

    @Override
    public Optional<DetalleVenta> buscarPorId(Integer id) {
        return detalleVentaRepository.findById(id);
    }

    @Override
    public List<DetalleVenta> buscarPorVentaId(Integer idVenta) {
        return detalleVentaRepository.findByVentasIdVenta(idVenta);
    }

    @Override
    public void guardar(DetalleVenta detalleVenta) {
        detalleVentaRepository.save(detalleVenta);
    }

    @Override
    public void eliminar(Integer id) {
        detalleVentaRepository.deleteById(id);
    }
}