package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.Compras;
import com.brandonorellana.Tienda_Suma.entity.DetalleCompra;
import com.brandonorellana.Tienda_Suma.repository.ComprasRepository;
import com.brandonorellana.Tienda_Suma.repository.DetalleCompraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ComprasServiceImpl implements ComprasService {

    @Autowired
    private ComprasRepository compraRepository;

    @Autowired
    private DetalleCompraRepository detalleCompraRepository;

    @Autowired
    private ProductosService productoService;

    @Override
    public List<Compras> listarTodos() {
        return compraRepository.findAll();
    }

    @Override
    public Optional<Compras> buscarPorId(Integer id) {
        return compraRepository.findById(id);
    }

    @Override
    public List<Compras> buscarPorUsuario(Integer idUsuario) {
        return compraRepository.findByUsuariosIdUsuario(idUsuario);
    }

    @Override
    public List<DetalleCompra> obtenerDetalles(Integer idCompra) {
        return detalleCompraRepository.findByComprasIdCompra(idCompra);
    }

    @Override
    public void guardar(Compras compra, List<DetalleCompra> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleCompra detalle : detalles) {
            BigDecimal subtotal = detalle.getPrecioCompra()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
            productoService.descontarStock(detalle.getProductosIdProducto(), -detalle.getCantidad());
        }
        compra.setTotal(total);

        Compras compraGuardada = compraRepository.save(compra);

        for (DetalleCompra detalle : detalles) {
            detalle.setComprasIdCompra(compraGuardada.getIdCompra());
            detalleCompraRepository.save(detalle);
        }
    }

    @Override
    public void eliminar(Integer id) {
        compraRepository.deleteById(id);
    }
}