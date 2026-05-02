package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.*;
import com.brandonorellana.Tienda_Suma.service.*;
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
@RequestMapping("/compras")
public class CompraController {

    @Autowired
    private ProductosService productosService;

    @Autowired
    private ComprasService comprasService;

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private ProveedoresService proveedoresService;

    @GetMapping("/mis-compras")
    public String misCompras(Model model, Authentication auth) {
        Usuarios cliente = usuariosService.buscarPorEmail(auth.getName()).get();
        List<Compras> compras = comprasService.buscarPorUsuario(cliente.getIdUsuario());
        model.addAttribute("compras", compras);
        return "compras/mis-compras";
    }

    @GetMapping("/nueva")
    public String nuevaCompra(Model model, Authentication auth) {
        String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");
        if (!rol.equals("CLIENTE")) {
            return "redirect:/home";
        }
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        return "compras/formulario";
    }

    @PostMapping("/guardar")
    public String guardarCompra(@RequestParam Integer proveedorId,
                                @RequestParam Map<String, String> params,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            Usuarios cliente = usuariosService.buscarPorEmail(auth.getName()).get();

            Compras compra = new Compras();
            compra.setProveedoresIdProveedor(proveedorId);
            compra.setUsuariosIdUsuario(cliente.getIdUsuario());
            compra.setTotal(BigDecimal.ZERO);

            List<DetalleCompra> detalles = new ArrayList<>();

            for (String key : params.keySet()) {
                if (key.startsWith("producto_")) {
                    Integer productoId = Integer.parseInt(key.substring(9));
                    Integer cantidad = Integer.parseInt(params.get(key));

                    if (cantidad > 0) {
                        Productos producto = productosService.buscarPorId(productoId).get();
                        DetalleCompra detalle = new DetalleCompra();
                        detalle.setProductosIdProducto(productoId);
                        detalle.setCantidad(cantidad);
                        detalle.setPrecioCompra(producto.getPrecio());
                        detalles.add(detalle);
                    }
                }
            }

            if (detalles.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un producto");
                return "redirect:/compras/nueva";
            }

            comprasService.guardar(compra, detalles);
            redirectAttributes.addFlashAttribute("success", "Compra realizada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/compras/mis-compras";
    }

    @GetMapping("/detalle/{id}")
    public String verDetalleCompra(@PathVariable Integer id, Model model, Authentication auth) {
        Compras compra = comprasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName()).get();
        if (!compra.getUsuariosIdUsuario().equals(usuario.getIdUsuario())) {
            model.addAttribute("error", "No tienes permisos para ver esta compra");
            return "redirect:/compras/mis-compras";
        }

        List<DetalleCompra> detalles = comprasService.obtenerDetalles(id);
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", detalles);
        return "compras/detalle";
    }
}