package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.entity.DetalleVenta;
import com.brandonorellana.Tienda_Suma.entity.Ventas;
import com.brandonorellana.Tienda_Suma.repository.DetalleVentaRepository;
import com.brandonorellana.Tienda_Suma.repository.VentasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class VentasServiceImpl implements VentasService {

    @Autowired
    private VentasRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Autowired
    private ProductosService productoService;

    @Override
    public List<Ventas> listarTodos() {
        return ventaRepository.findAll();
    }

    @Override
    public Optional<Ventas> buscarPorId(Integer id) {
        return ventaRepository.findById(id);
    }

    @Override
    public List<Ventas> buscarPorCliente(Integer idCliente) {
        return ventaRepository.findByUsuariosIdUsuario(idCliente);
    }

    @Override
    public void registrarVenta(Ventas venta, List<DetalleVenta> detalles) {
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVenta detalle : detalles) {
            BigDecimal subtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
            // RESTA stock (cantidad positiva)
            productoService.descontarStock(detalle.getProductosIdProducto(), detalle.getCantidad());
        }
        venta.setTotal(total);

        Ventas ventaGuardada = ventaRepository.save(venta);

        for (DetalleVenta detalle : detalles) {
            detalle.setVentasIdVenta(ventaGuardada.getIdVenta());
            detalleVentaRepository.save(detalle);
        }
    }

    @Override
    public BigDecimal calcularTotalVenta(Integer idVenta) {
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentasIdVenta(idVenta);
        BigDecimal total = BigDecimal.ZERO;
        for (DetalleVenta detalle : detalles) {
            BigDecimal subtotal = detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad()));
            total = total.add(subtotal);
        }
        return total;
    }

    @Override
    public void eliminar(Integer id) {
        ventaRepository.deleteById(id);
    }
}