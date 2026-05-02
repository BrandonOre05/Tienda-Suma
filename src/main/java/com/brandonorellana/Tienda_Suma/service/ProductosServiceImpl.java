package com.brandonorellana.Tienda_Suma.service;
import com.brandonorellana.Tienda_Suma.entity.Productos;
import com.brandonorellana.Tienda_Suma.repository.ProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductosServiceImpl implements ProductosService {

    @Autowired
    private ProductosRepository productoRepository;

    @Override
    public List<Productos> listarTodos() {
        return productoRepository.findAll();
    }

    @Override
    public Optional<Productos> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }

    @Override
    public void guardar(Productos producto) {
        productoRepository.save(producto);
    }

    @Override
    public void actualizar(Productos producto) {
        productoRepository.save(producto);
    }

    @Override
    public void eliminar(Integer id) {
        productoRepository.deleteById(id);
    }

    @Override
    public boolean hayStockSuficiente(Integer id, int cantidad) {
        Optional<Productos> producto = productoRepository.findById(id);
        return producto.isPresent() && producto.get().getStock() >= cantidad;
    }

    @Override
    public void descontarStock(Integer id, int cantidad) {
        Optional<Productos> producto = productoRepository.findById(id);
        if (producto.isPresent()) {
            Productos p = producto.get();
            p.setStock(p.getStock() - cantidad);
            productoRepository.save(p);
        }
    }
}