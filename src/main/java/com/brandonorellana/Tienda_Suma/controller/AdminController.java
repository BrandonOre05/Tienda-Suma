package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.Categorias;
import com.brandonorellana.Tienda_Suma.entity.Productos;
import com.brandonorellana.Tienda_Suma.entity.Proveedores;
import com.brandonorellana.Tienda_Suma.entity.Rol;
import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import com.brandonorellana.Tienda_Suma.service.CategoriasService;
import com.brandonorellana.Tienda_Suma.service.ProductosService;
import com.brandonorellana.Tienda_Suma.service.ProveedoresService;
import com.brandonorellana.Tienda_Suma.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
                // Actualizar existente
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
                // Crear nuevo
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
}