package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.DetalleVenta;
import com.brandonorellana.Tienda_Suma.entity.Productos;
import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import com.brandonorellana.Tienda_Suma.entity.Ventas;
import com.brandonorellana.Tienda_Suma.service.ProductosService;
import com.brandonorellana.Tienda_Suma.service.UsuariosService;
import com.brandonorellana.Tienda_Suma.service.VentasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private VentasService ventasService;

    @Autowired
    private ProductosService productosService;

    @Autowired
    private UsuariosService usuariosService;

    // Listar ventas
    @GetMapping
    public String listarVentas(Model model, Authentication auth) {
        String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");
        List<Ventas> ventas;

        if (rol.equals("ADMIN")) {
            ventas = ventasService.listarTodos();
        } else {
            // Para vendedor, solo sus propias ventas
            Usuarios usuario = usuariosService.buscarPorEmail(auth.getName()).get();
            ventas = ventasService.buscarPorCliente(usuario.getIdUsuario());
        }

        model.addAttribute("ventas", ventas);
        model.addAttribute("rol", rol);
        return "ventas/lista";
    }

    // Formulario nueva venta
    @GetMapping("/nueva")
    public String mostrarFormVenta(Model model, Authentication auth, RedirectAttributes redirectAttributes) {
        String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");
        if (!rol.equals("ADMIN") && !rol.equals("VENDEDOR")) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para registrar ventas");
            return "redirect:/ventas";
        }

        model.addAttribute("venta", new Ventas());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("clientes", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().toString().equals("CLIENTE"))
                .toList());
        return "ventas/formulario";
    }

    // Guardar venta
    @PostMapping("/guardar")
    public String guardarVenta(@RequestParam Integer clienteId,
                               @RequestParam Map<String, String> params,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        try {
            String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");
            if (!rol.equals("ADMIN") && !rol.equals("VENDEDOR")) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para registrar ventas");
                return "redirect:/ventas";
            }

            Usuarios vendedor = usuariosService.buscarPorEmail(auth.getName()).get();

            Ventas venta = new Ventas();
            venta.setUsuariosIdUsuario(clienteId);
            venta.setVendedorIdUsuario(vendedor.getIdUsuario());
            venta.setTotal(BigDecimal.ZERO);

            List<DetalleVenta> detalles = new ArrayList<>();

            // Procesar productos seleccionados
            for (String key : params.keySet()) {
                if (key.startsWith("producto_")) {
                    Integer productoId = Integer.parseInt(key.substring(9));
                    Integer cantidad = Integer.parseInt(params.get(key));

                    if (cantidad > 0) {
                        Productos producto = productosService.buscarPorId(productoId).get();
                        if (productosService.hayStockSuficiente(productoId, cantidad)) {
                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setProductosIdProducto(productoId);
                            detalle.setCantidad(cantidad);
                            detalle.setPrecioUnitario(producto.getPrecio());
                            detalles.add(detalle);
                        } else {
                            redirectAttributes.addFlashAttribute("error",
                                    "Stock insuficiente para " + producto.getNombreProducto());
                            return "redirect:/ventas/nueva";
                        }
                    }
                }
            }

            if (detalles.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un producto");
                return "redirect:/ventas/nueva";
            }

            ventasService.registrarVenta(venta, detalles);
            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al registrar la venta: " + e.getMessage());
        }

        return "redirect:/ventas";
    }

    // Ver detalle venta
    @GetMapping("/detalle/{id}")
    public String verDetalleVenta(@PathVariable Integer id, Model model, Authentication auth) {
        Ventas venta = ventasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");
        if (!rol.equals("ADMIN")) {
            Usuarios usuario = usuariosService.buscarPorEmail(auth.getName()).get();
            if (!venta.getVendedorIdUsuario().equals(usuario.getIdUsuario())) {
                model.addAttribute("error", "No tienes permisos para ver esta venta");
                return "redirect:/ventas";
            }
        }

        model.addAttribute("venta", venta);
        model.addAttribute("total", ventasService.calcularTotalVenta(id));
        model.addAttribute("rol", rol);
        return "ventas/detalle";
    }

    // Eliminar venta (solo ADMIN)
    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Integer id,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");
        if (!rol.equals("ADMIN")) {
            redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar ventas");
            return "redirect:/ventas";
        }

        ventasService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Venta eliminada exitosamente");
        return "redirect:/ventas";
    }
}