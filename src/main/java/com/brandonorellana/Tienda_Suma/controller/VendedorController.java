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
@RequestMapping("/vendedor")
public class VendedorController {

    @Autowired
    private ProductosService productosService;

    @Autowired
    private VentasService ventasService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private UsuariosService usuariosService;

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
        return "vendedor/productos/lista";
    }

    @GetMapping("/productos/detalle/{id}")
    public String verDetalleProducto(@PathVariable Integer id, Model model) {
        Productos producto = productosService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "vendedor/productos/detalle";
    }

    // ============================================================
    // ====================== VENTAS CRUD COMPLETO ======================
    // ============================================================

    // Listar mis ventas
    @GetMapping("/ventas")
    public String misVentas(Model model, Authentication auth) {
        Usuarios vendedor = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        List<Ventas> ventas = ventasService.buscarPorVendedor(vendedor.getIdUsuario());
        model.addAttribute("ventas", ventas);
        return "vendedor/ventas/lista";
    }

    // Formulario para nueva venta
    @GetMapping("/ventas/nueva")
    public String nuevaVenta(Model model) {
        model.addAttribute("venta", new Ventas());
        model.addAttribute("productos", productosService.listarTodos().stream()
                .filter(p -> p.getEstado() && p.getStock() > 0)
                .collect(Collectors.toList()));
        model.addAttribute("clientes", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().equals(Rol.CLIENTE) && u.getEstado())
                .collect(Collectors.toList()));
        return "vendedor/ventas/formulario";
    }

    // Guardar nueva venta
    @PostMapping("/ventas/guardar")
    public String guardarVenta(@RequestParam Integer clienteId,
                               @RequestParam Map<String, String> params,
                               Authentication auth,
                               RedirectAttributes redirectAttributes) {
        try {
            Usuarios vendedor = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Ventas venta = new Ventas();
            venta.setUsuariosIdUsuario(clienteId);
            venta.setVendedorIdUsuario(vendedor.getIdUsuario());
            venta.setTotal(BigDecimal.ZERO);

            List<DetalleVenta> detalles = new ArrayList<>();
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
                                return "redirect:/vendedor/ventas/nueva";
                            }

                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setProductosIdProducto(productoId);
                            detalle.setCantidad(cantidad);
                            detalle.setPrecioUnitario(producto.getPrecio());
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
                return "redirect:/vendedor/ventas/nueva";
            }

            venta.setTotal(total);
            ventasService.registrarVenta(venta, detalles);
            redirectAttributes.addFlashAttribute("success", "Venta registrada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/vendedor/ventas";
    }

    // Ver detalle de venta
    @GetMapping("/ventas/detalle/{id}")
    public String verDetalleVenta(@PathVariable Integer id, Model model, Authentication auth) {
        Ventas venta = ventasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!venta.getVendedorIdUsuario().equals(usuario.getIdUsuario())) {
            model.addAttribute("error", "No tienes permisos para ver esta venta");
            return "redirect:/vendedor/ventas";
        }

        List<DetalleVenta> detalles = detalleVentaService.buscarPorVentaId(id);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", ventasService.calcularTotalVenta(id));
        return "vendedor/ventas/detalle";
    }

    // Formulario para editar venta
    @GetMapping("/ventas/editar/{id}")
    public String editarVenta(@PathVariable Integer id, Model model, Authentication auth) {
        Ventas venta = ventasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!venta.getVendedorIdUsuario().equals(usuario.getIdUsuario())) {
            model.addAttribute("error", "No tienes permisos para editar esta venta");
            return "redirect:/vendedor/ventas";
        }

        List<DetalleVenta> detalles = detalleVentaService.buscarPorVentaId(id);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        model.addAttribute("productos", productosService.listarTodos().stream()
                .filter(p -> p.getEstado())
                .collect(Collectors.toList()));
        model.addAttribute("clientes", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().equals(Rol.CLIENTE) && u.getEstado())
                .collect(Collectors.toList()));
        return "vendedor/ventas/editar";
    }

    // Actualizar venta
    @PostMapping("/ventas/actualizar/{id}")
    public String actualizarVenta(@PathVariable Integer id,
                                  @RequestParam Integer clienteId,
                                  @RequestParam Map<String, String> params,
                                  Authentication auth,
                                  RedirectAttributes redirectAttributes) {
        try {
            Usuarios vendedor = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Ventas venta = ventasService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            if (!venta.getVendedorIdUsuario().equals(vendedor.getIdUsuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para editar esta venta");
                return "redirect:/vendedor/ventas";
            }

            // Eliminar detalles existentes y revertir stock
            List<DetalleVenta> detallesExistentes = detalleVentaService.buscarPorVentaId(id);
            for (DetalleVenta det : detallesExistentes) {
                Productos producto = productosService.buscarPorId(det.getProductosIdProducto()).get();
                producto.setStock(producto.getStock() + det.getCantidad());
                productosService.guardar(producto);
                detalleVentaService.eliminar(det.getIdDetalleVenta());
            }

            // Crear nuevos detalles
            List<DetalleVenta> nuevosDetalles = new ArrayList<>();
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
                                return "redirect:/vendedor/ventas/editar/" + id;
                            }

                            DetalleVenta detalle = new DetalleVenta();
                            detalle.setProductosIdProducto(productoId);
                            detalle.setCantidad(cantidad);
                            detalle.setPrecioUnitario(producto.getPrecio());
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
                return "redirect:/vendedor/ventas/editar/" + id;
            }

            venta.setUsuariosIdUsuario(clienteId);
            venta.setTotal(total);
            ventasService.registrarVenta(venta, nuevosDetalles);
            redirectAttributes.addFlashAttribute("success", "Venta actualizada exitosamente");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/vendedor/ventas";
    }

    // Eliminar venta
    @GetMapping("/ventas/eliminar/{id}")
    public String eliminarVenta(@PathVariable Integer id, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            Usuarios vendedor = usuariosService.buscarPorEmail(auth.getName())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Ventas venta = ventasService.buscarPorId(id)
                    .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

            if (!venta.getVendedorIdUsuario().equals(vendedor.getIdUsuario())) {
                redirectAttributes.addFlashAttribute("error", "No tienes permisos para eliminar esta venta");
                return "redirect:/vendedor/ventas";
            }

            // Revertir stock antes de eliminar
            List<DetalleVenta> detalles = detalleVentaService.buscarPorVentaId(id);
            for (DetalleVenta detalle : detalles) {
                Productos producto = productosService.buscarPorId(detalle.getProductosIdProducto()).get();
                producto.setStock(producto.getStock() + detalle.getCantidad());
                productosService.guardar(producto);
            }

            ventasService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Venta eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/vendedor/ventas";
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