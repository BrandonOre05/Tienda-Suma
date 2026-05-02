package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.*;
import com.brandonorellana.Tienda_Suma.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ProductosService productosService;

    @Autowired
    private ComprasService comprasService;

    @Autowired
    private DetalleCompraService detalleCompraService;

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private ProveedoresService proveedoresService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ============================================================
    // ====================== PRODUCTOS ======================
    // ============================================================

    @GetMapping("/productos")
    public String listarProductos(@RequestParam(required = false) String search,
                                  Model model) {
        List<Productos> productos;
        if (search != null && !search.isEmpty()) {
            productos = productosService.listarTodos().stream()
                    .filter(p -> p.getEstado() && (
                            p.getNombreProducto().toLowerCase().contains(search.toLowerCase()) ||
                                    (p.getDescripcion() != null && p.getDescripcion().toLowerCase().contains(search.toLowerCase()))))
                    .collect(Collectors.toList());
            model.addAttribute("search", search);
        } else {
            productos = productosService.listarTodos().stream()
                    .filter(p -> p.getEstado())
                    .collect(Collectors.toList());
        }

        model.addAttribute("productos", productos);
        return "cliente/productos/lista";
    }

    @GetMapping("/productos/detalle/{id}")
    public String verDetalleProducto(@PathVariable Integer id, Model model) {
        Productos producto = productosService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "cliente/productos/detalle";
    }

    // ============================================================
    // ====================== COMPRAS CRUD COMPLETO ======================
    // ============================================================

    @GetMapping("/compras")
    public String misCompras(Model model, Authentication auth) {
        Usuarios cliente = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Compras> compras = comprasService.buscarPorUsuario(cliente.getIdUsuario());
        model.addAttribute("compras", compras);
        return "cliente/compras/lista";
    }

    @GetMapping("/compras/nueva")
    public String nuevaCompra(Model model) {
        model.addAttribute("productos", productosService.listarTodos().stream()
                .filter(p -> p.getEstado() && p.getStock() > 0)
                .collect(Collectors.toList()));
        model.addAttribute("compra", new Compras());
        return "cliente/compras/formulario";
    }

    @PostMapping("/compras/guardar")
    public String guardarCompra(@RequestParam Map<String, String> params,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            Usuarios cliente = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Compras compra = new Compras();
            compra.setUsuariosIdUsuario(cliente.getIdUsuario());
            compra.setTotal(BigDecimal.ZERO);

            // Asignar proveedor por defecto
            List<Proveedores> proveedores = proveedoresService.listarTodos();
            if (!proveedores.isEmpty()) {
                compra.setProveedoresIdProveedor(proveedores.get(0).getIdProveedor());
            } else {
                compra.setProveedoresIdProveedor(1);
            }

            List<DetalleCompra> detalles = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (String key : params.keySet()) {
                if (key.startsWith("producto_")) {
                    Integer productoId = Integer.parseInt(key.substring(9));
                    String cantidadStr = params.get(key);

                    if (cantidadStr != null && !cantidadStr.isEmpty()) {
                        Integer cantidad = Integer.parseInt(cantidadStr);

                        if (cantidad > 0) {
                            Productos producto = productosService.buscarPorId(productoId)
                                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                            if (producto.getStock() < cantidad) {
                                redirectAttributes.addFlashAttribute("error",
                                        "Stock insuficiente para " + producto.getNombreProducto());
                                return "redirect:/cliente/compras/nueva";
                            }

                            DetalleCompra detalle = new DetalleCompra();
                            detalle.setProductosIdProducto(productoId);
                            detalle.setCantidad(cantidad);
                            detalle.setPrecioCompra(producto.getPrecio());
                            detalles.add(detalle);

                            BigDecimal subtotal = producto.getPrecio()
                                    .multiply(BigDecimal.valueOf(cantidad));
                            total = total.add(subtotal);
                        }
                    }
                }
            }

            if (detalles.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un producto");
                return "redirect:/cliente/compras/nueva";
            }

            compra.setTotal(total);
            comprasService.guardar(compra, detalles);
            redirectAttributes.addFlashAttribute("success", "Compra realizada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/cliente/compras";
    }

    @GetMapping("/compras/detalle/{id}")
    public String verDetalleCompra(@PathVariable Integer id, Model model, Authentication auth) {
        Compras compra = comprasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!compra.getUsuariosIdUsuario().equals(usuario.getIdUsuario())) {
            model.addAttribute("error", "No tienes permisos para ver esta compra");
            return "redirect:/cliente/compras";
        }

        List<DetalleCompra> detalles = detalleCompraService.buscarPorCompraId(id);
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", detalles);
        return "cliente/compras/detalle";
    }

    @GetMapping("/compras/editar/{id}")
    public String editarCompra(@PathVariable Integer id, Model model, Authentication auth) {
        Compras compra = comprasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!compra.getUsuariosIdUsuario().equals(usuario.getIdUsuario())) {
            model.addAttribute("error", "No tienes permisos para editar esta compra");
            return "redirect:/cliente/compras";
        }

        List<DetalleCompra> detalles = detalleCompraService.buscarPorCompraId(id);
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", detalles);
        model.addAttribute("productos", productosService.listarTodos().stream()
                .filter(p -> p.getEstado())
                .collect(Collectors.toList()));
        return "cliente/compras/editar";
    }

    @PostMapping("/compras/actualizar/{id}")
    public String actualizarCompra(@PathVariable Integer id,
                                   @RequestParam Map<String, String> params,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuarios cliente = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Compras compra = comprasService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            if (!compra.getUsuariosIdUsuario().equals(cliente.getIdUsuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar esta compra");
                return "redirect:/cliente/compras";
            }

            // Eliminar detalles existentes y revertir stock
            List<DetalleCompra> detallesExistentes = detalleCompraService.buscarPorCompraId(id);
            for (DetalleCompra det : detallesExistentes) {
                Productos producto = productosService.buscarPorId(det.getProductosIdProducto()).get();
                producto.setStock(producto.getStock() + det.getCantidad());
                productosService.guardar(producto);
                detalleCompraService.eliminar(det.getIdDetalleCompra());
            }

            // Crear nuevos detalles
            List<DetalleCompra> nuevosDetalles = new ArrayList<>();
            BigDecimal total = BigDecimal.ZERO;

            for (String key : params.keySet()) {
                if (key.startsWith("producto_")) {
                    Integer productoId = Integer.parseInt(key.substring(9));
                    String cantidadStr = params.get(key);

                    if (cantidadStr != null && !cantidadStr.isEmpty()) {
                        Integer cantidad = Integer.parseInt(cantidadStr);

                        if (cantidad > 0) {
                            Productos producto = productosService.buscarPorId(productoId)
                                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                            if (producto.getStock() < cantidad) {
                                redirectAttributes.addFlashAttribute("error",
                                        "Stock insuficiente para " + producto.getNombreProducto());
                                return "redirect:/cliente/compras/editar/" + id;
                            }

                            DetalleCompra detalle = new DetalleCompra();
                            detalle.setProductosIdProducto(productoId);
                            detalle.setCantidad(cantidad);
                            detalle.setPrecioCompra(producto.getPrecio());
                            nuevosDetalles.add(detalle);

                            BigDecimal subtotal = producto.getPrecio()
                                    .multiply(BigDecimal.valueOf(cantidad));
                            total = total.add(subtotal);
                        }
                    }
                }
            }

            if (nuevosDetalles.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar al menos un producto");
                return "redirect:/cliente/compras/editar/" + id;
            }

            compra.setTotal(total);
            comprasService.guardar(compra, nuevosDetalles);
            redirectAttributes.addFlashAttribute("success", "Compra actualizada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/cliente/compras";
    }

    @GetMapping("/compras/eliminar/{id}")
    public String eliminarCompra(@PathVariable Integer id, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Usuarios cliente = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Compras compra = comprasService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Compra no encontrada"));

            if (!compra.getUsuariosIdUsuario().equals(cliente.getIdUsuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar esta compra");
                return "redirect:/cliente/compras";
            }

            List<DetalleCompra> detalles = detalleCompraService.buscarPorCompraId(id);
            for (DetalleCompra detalle : detalles) {
                Productos producto = productosService.buscarPorId(detalle.getProductosIdProducto()).get();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productosService.guardar(producto);
            }

            comprasService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Compra eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/cliente/compras";
    }

    // ============================================================
    // ====================== PERFIL ======================
    // ============================================================

    @GetMapping("/perfil")
    public String verPerfil(Model model, Authentication auth) {
        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/perfil/actualizar")
    public String actualizarPerfil(@RequestParam String nombre,
                                   @RequestParam String apellido,
                                   @RequestParam(required = false) String telefono,
                                   @RequestParam(required = false) String direccion,
                                   @RequestParam(required = false) String nuevaPassword,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuarios usuarioExistente = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            usuarioExistente.setNombre(nombre);
            usuarioExistente.setApellido(apellido);
            usuarioExistente.setTelefono(telefono);
            usuarioExistente.setDireccion(direccion);

            if (nuevaPassword != null && !nuevaPassword.isEmpty()) {
                usuarioExistente.setPassword(passwordEncoder.encode(nuevaPassword));
            }

            usuariosService.actualizar(usuarioExistente);
            redirectAttributes.addFlashAttribute("success", "Perfil actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/perfil";
    }
}