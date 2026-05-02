package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.DetalleCompra;
import com.brandonorellana.Tienda_Suma.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class DetalleCompraServiceImpl implements DetalleCompraService {

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Override
    public List<DetalleCompra> listarTodos() {
        return detalleCompraRepository.findAll();
    }

    @Override
    public Optional<DetalleCompra> buscarPorId(Integer id) {
        return detalleCompraRepository.findById(id);
    }

    @Override
    public List<DetalleCompra> buscarPorCompraId(Integer idCompra) {
        return detalleCompraRepository.findByComprasIdCompra(idCompra);
    }

    @Override
    public void guardar(DetalleCompra detalleCompra) {
        detalleCompraRepository.save(detalleCompra);
    }

    @Override
    public void eliminar(Integer id) {
        detalleCompraRepository.deleteById(id);
    }
}