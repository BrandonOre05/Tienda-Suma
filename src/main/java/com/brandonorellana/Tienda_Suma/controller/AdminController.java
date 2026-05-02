package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.*;
import com.brandonorellana.Tienda_Suma.service.*;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private ProveedoresService proveedoresService;

    @Autowired
    private CategoriasService categoriasService;

    @Autowired
    private ProductosService productosService;

    @Autowired
    private ComprasService comprasService;

    @Autowired
    private DetalleCompraService detalleCompraService;

    @Autowired
    private VentasService ventasService;

    @Autowired
    private DetalleVentaService detalleVentaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ============================================================
    // ====================== PRODUCTOS CRUD ======================
    // ============================================================

    @GetMapping("/productos")
    public String listarProductosAdmin(Model model) {
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("categorias", categoriasService.listarTodos());
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        return "admin/productos/lista";
    }

    @GetMapping("/productos/nuevo")
    public String nuevoProductoAdmin(Model model) {
        model.addAttribute("producto", new Productos());
        model.addAttribute("categorias", categoriasService.listarTodos());
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        return "admin/productos/formulario";
    }

    @GetMapping("/productos/editar/{id}")
    public String editarProductoAdmin(@PathVariable Integer id, Model model) {
        Productos producto = productosService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        model.addAttribute("categorias", categoriasService.listarTodos());
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        return "admin/productos/formulario";
    }

    @PostMapping("/productos/guardar")
    public String guardarProductoAdmin(@ModelAttribute Productos producto,
                                       @RequestParam(required = false) Integer idProducto,
                                       RedirectAttributes redirectAttributes) {
        try {
            if (idProducto != null) {
                producto.setIdProducto(idProducto);
            }
            producto.setEstado(true);
            productosService.guardar(producto);
            redirectAttributes.addFlashAttribute("success", "Producto guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/productos";
    }

    @GetMapping("/productos/eliminar/{id}")
    public String eliminarProductoAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        productosService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Producto eliminado exitosamente");
        return "redirect:/admin/productos";
    }

    // ============================================================
    // ====================== CATEGORÍAS CRUD ======================
    // ============================================================

    @GetMapping("/categorias")
    public String listarCategoriasAdmin(Model model) {
        model.addAttribute("categorias", categoriasService.listarTodos());
        return "admin/categorias/lista";
    }

    @GetMapping("/categorias/nuevo")
    public String nuevaCategoriaAdmin(Model model) {
        model.addAttribute("categoria", new Categorias());
        return "admin/categorias/formulario";
    }

    @GetMapping("/categorias/editar/{id}")
    public String editarCategoriaAdmin(@PathVariable Integer id, Model model) {
        Categorias categoria = categoriasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        model.addAttribute("categoria", categoria);
        return "admin/categorias/formulario";
    }

    @PostMapping("/categorias/guardar")
    public String guardarCategoriaAdmin(@ModelAttribute Categorias categoria,
                                        @RequestParam(required = false) Integer idCategoria,
                                        RedirectAttributes redirectAttributes) {
        try {
            if (idCategoria != null) {
                categoria.setIdCategoria(idCategoria);
            }
            categoriasService.guardar(categoria);
            redirectAttributes.addFlashAttribute("success", "Categoría guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/categorias";
    }

    @GetMapping("/categorias/eliminar/{id}")
    public String eliminarCategoriaAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        categoriasService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Categoría eliminada exitosamente");
        return "redirect:/admin/categorias";
    }

    // ============================================================
    // ====================== PROVEEDORES CRUD ======================
    // ============================================================

    @GetMapping("/proveedores")
    public String listarProveedoresAdmin(Model model) {
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        return "admin/proveedores/lista";
    }

    @GetMapping("/proveedores/nuevo")
    public String nuevoProveedorAdmin(Model model) {
        model.addAttribute("proveedor", new Proveedores());
        return "admin/proveedores/formulario";
    }

    @GetMapping("/proveedores/editar/{id}")
    public String editarProveedorAdmin(@PathVariable Integer id, Model model) {
        Proveedores proveedor = proveedoresService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        model.addAttribute("proveedor", proveedor);
        return "admin/proveedores/formulario";
    }

    @PostMapping("/proveedores/guardar")
    public String guardarProveedorAdmin(@ModelAttribute Proveedores proveedor,
                                        @RequestParam(required = false) Integer idProveedor,
                                        RedirectAttributes redirectAttributes) {
        try {
            if (idProveedor != null) {
                proveedor.setIdProveedor(idProveedor);
            }
            proveedor.setEstado(true);
            proveedoresService.guardar(proveedor);
            redirectAttributes.addFlashAttribute("success", "Proveedor guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/proveedores";
    }

    @GetMapping("/proveedores/eliminar/{id}")
    public String eliminarProveedorAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        proveedoresService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Proveedor eliminado exitosamente");
        return "redirect:/admin/proveedores";
    }

    // ============================================================
    // ====================== USUARIOS CRUD ======================
    // ============================================================

    @GetMapping("/usuarios")
    public String listarUsuariosAdmin(Model model) {
        model.addAttribute("usuarios", usuariosService.listarTodos());
        return "admin/usuarios/lista";
    }

    @GetMapping("/usuarios/nuevo")
    public String nuevoUsuarioAdmin(Model model) {
        model.addAttribute("usuario", new Usuarios());
        model.addAttribute("roles", Rol.values());
        return "admin/usuarios/formulario";
    }

    @GetMapping("/usuarios/editar/{id}")
    public String editarUsuarioAdmin(@PathVariable Integer id, Model model) {
        Usuarios usuario = usuariosService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        model.addAttribute("usuario", usuario);
        model.addAttribute("roles", Rol.values());
        return "admin/usuarios/formulario";
    }

    @PostMapping("/usuarios/guardar")
    public String guardarUsuarioAdmin(@ModelAttribute Usuarios usuario,
                                      @RequestParam(required = false) String nuevaPassword,
                                      @RequestParam(required = false) Integer idUsuario,
                                      RedirectAttributes redirectAttributes) {
        try {
            if (idUsuario != null && idUsuario > 0) {
                Usuarios existing = usuariosService.buscarPorId(idUsuario).get();
                existing.setNombre(usuario.getNombre());
                existing.setApellido(usuario.getApellido());
                existing.setTelefono(usuario.getTelefono());
                existing.setDireccion(usuario.getDireccion());
                existing.setRol(usuario.getRol());
                existing.setEstado(usuario.getEstado());
                if (nuevaPassword != null && !nuevaPassword.isEmpty()) {
                    existing.setPassword(passwordEncoder.encode(nuevaPassword));
                }
                usuariosService.actualizar(existing);
            } else {
                usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
                usuario.setEstado(true);
                usuariosService.crear(usuario);
            }
            redirectAttributes.addFlashAttribute("success", "Usuario guardado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @GetMapping("/usuarios/eliminar/{id}")
    public String eliminarUsuarioAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        usuariosService.eliminar(id);
        redirectAttributes.addFlashAttribute("success", "Usuario eliminado exitosamente");
        return "redirect:/admin/usuarios";
    }

    // ============================================================
    // ====================== COMPRAS CRUD ======================
    // ============================================================

    @GetMapping("/compras")
    public String listarComprasAdmin(Model model) {
        model.addAttribute("compras", comprasService.listarTodos());
        return "admin/compras/lista";
    }

    @GetMapping("/compras/nueva")
    public String nuevaCompraAdmin(Model model) {
        model.addAttribute("compra", new Compras());
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("usuarios", usuariosService.listarTodos());
        return "admin/compras/formulario";
    }

    @GetMapping("/compras/editar/{id}")
    public String editarCompraAdmin(@PathVariable Integer id, Model model) {
        Compras compra = comprasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        List<DetalleCompra> detalles = detalleCompraService.buscarPorCompraId(id);
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", detalles);
        model.addAttribute("proveedores", proveedoresService.listarTodos());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("usuarios", usuariosService.listarTodos());
        return "admin/compras/formulario";
    }

    @PostMapping("/compras/guardar")
    public String guardarCompraAdmin(@ModelAttribute Compras compra,
                                     @RequestParam(required = false) Integer idCompra,
                                     @RequestParam Map<String, String> params,
                                     RedirectAttributes redirectAttributes) {
        try {
            boolean esEdicion = (idCompra != null && idCompra > 0);

            if (esEdicion) {
                compra.setIdCompra(idCompra);
                // Si es edición, primero eliminar los detalles existentes
                List<DetalleCompra> detallesExistentes = detalleCompraService.buscarPorCompraId(idCompra);
                for (DetalleCompra det : detallesExistentes) {
                    detalleCompraService.eliminar(det.getIdDetalleCompra());
                }
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
                                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

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
                return "redirect:/admin/compras" + (esEdicion ? "/editar/" + idCompra : "/nueva");
            }

            compra.setTotal(total);

            // Guardar la compra y sus detalles
            comprasService.guardar(compra, detalles);

            redirectAttributes.addFlashAttribute("success", esEdicion ? "Compra actualizada exitosamente" : "Compra guardada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/admin/compras";
    }

    @GetMapping("/compras/detalle/{id}")
    public String verDetalleCompraAdmin(@PathVariable Integer id, Model model) {
        Compras compra = comprasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada"));
        List<DetalleCompra> detalles = detalleCompraService.buscarPorCompraId(id);
        model.addAttribute("compra", compra);
        model.addAttribute("detalles", detalles);
        return "admin/compras/detalle";
    }

    @GetMapping("/compras/eliminar/{id}")
    public String eliminarCompraAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            comprasService.eliminar(id);
            redirectAttributes.addFlashAttribute("success", "Compra eliminada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/compras";
    }

    // ============================================================
    // ====================== VENTAS CRUD ======================
    // ============================================================

    // ============================================================
// ====================== VENTAS CRUD COMPLETO ======================
// ============================================================

    @GetMapping("/ventas")
    public String listarVentasAdmin(Model model) {
        model.addAttribute("ventas", ventasService.listarTodos());
        return "admin/ventas/lista";
    }

    @GetMapping("/ventas/nueva")
    public String nuevaVentaAdmin(Model model) {
        model.addAttribute("venta", new Ventas());
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("clientes", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().equals(Rol.CLIENTE))
                .collect(Collectors.toList()));
        model.addAttribute("vendedores", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().equals(Rol.VENDEDOR) || u.getRol().equals(Rol.ADMIN))
                .collect(Collectors.toList()));
        return "admin/ventas/formulario";
    }

    @GetMapping("/ventas/editar/{id}")
    public String editarVentaAdmin(@PathVariable Integer id, Model model) {
        Ventas venta = ventasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        List<DetalleVenta> detalles = detalleVentaService.buscarPorVentaId(id);

        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        model.addAttribute("productos", productosService.listarTodos());
        model.addAttribute("clientes", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().equals(Rol.CLIENTE))
                .collect(Collectors.toList()));
        model.addAttribute("vendedores", usuariosService.listarTodos().stream()
                .filter(u -> u.getRol().equals(Rol.VENDEDOR) || u.getRol().equals(Rol.ADMIN))
                .collect(Collectors.toList()));
        return "admin/ventas/formulario";
    }

    @PostMapping("/ventas/guardar")
    public String guardarVentaAdmin(@ModelAttribute Ventas venta,
                                    @RequestParam(required = false) Integer idVenta,
                                    @RequestParam Map<String, String> params,
                                    RedirectAttributes redirectAttributes) {
        try {
            boolean esEdicion = (idVenta != null && idVenta > 0);

            if (esEdicion) {
                venta.setIdVenta(idVenta);
                // Si es edición, primero eliminar los detalles existentes
                List<DetalleVenta> detallesExistentes = detalleVentaService.buscarPorVentaId(idVenta);

                // Revertir stock de los detalles existentes
                for (DetalleVenta det : detallesExistentes) {
                    Productos producto = productosService.buscarPorId(det.getProductosIdProducto()).get();
                    producto.setStock(producto.getStock() + det.getCantidad()); // Devolver stock
                    productosService.guardar(producto);
                    detalleVentaService.eliminar(det.getIdDetalleVenta());
                }
            }

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
                                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + productoId));

                            // Validar stock suficiente
                            if (producto.getStock() < cantidad) {
                                redirectAttributes.addFlashAttribute("error",
                                        "Stock insuficiente para " + producto.getNombreProducto() + ". Disponible: " + producto.getStock());
                                return "redirect:/admin/ventas" + (esEdicion ? "/editar/" + idVenta : "/nueva");
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
                return "redirect:/admin/ventas" + (esEdicion ? "/editar/" + idVenta : "/nueva");
            }

            venta.setTotal(total);
            ventasService.registrarVenta(venta, detalles);

            redirectAttributes.addFlashAttribute("success", esEdicion ? "Venta actualizada exitosamente" : "Venta registrada exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/admin/ventas";
    }

    @GetMapping("/ventas/detalle/{id}")
    public String verDetalleVentaAdmin(@PathVariable Integer id, Model model) {
        Ventas venta = ventasService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));
        List<DetalleVenta> detalles = detalleVentaService.buscarPorVentaId(id);
        model.addAttribute("venta", venta);
        model.addAttribute("detalles", detalles);
        model.addAttribute("total", ventasService.calcularTotalVenta(id));
        return "admin/ventas/detalle";
    }

    @GetMapping("/ventas/eliminar/{id}")
    public String eliminarVentaAdmin(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
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
        return "redirect:/admin/ventas";
    }
}