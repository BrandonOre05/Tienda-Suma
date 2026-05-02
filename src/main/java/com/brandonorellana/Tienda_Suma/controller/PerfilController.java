package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import com.brandonorellana.Tienda_Suma.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private UsuariosService usuariosService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String verPerfil(Model model, Authentication auth) {
        Usuarios usuario = usuariosService.buscarPorEmail(auth.getName()).get();
        model.addAttribute("usuario", usuario);
        return "perfil";
    }

    @PostMapping("/actualizar")
    public String actualizarPerfil(@ModelAttribute Usuarios usuarioActualizado,
                                   @RequestParam(required = false) String nuevaPassword,
                                   Authentication auth,
                                   RedirectAttributes redirectAttributes) {
        try {
            Usuarios usuarioExistente = usuariosService.buscarPorEmail(auth.getName()).get();

            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellido(usuarioActualizado.getApellido());
            usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
            usuarioExistente.setDireccion(usuarioActualizado.getDireccion());

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