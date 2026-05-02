package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.entity.Rol;
import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import com.brandonorellana.Tienda_Suma.service.UsuariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UsuariosService usuariosService;

    @GetMapping("/home")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        String rol = auth.getAuthorities().iterator().next().toString().replace("ROLE_", "");

        // Obtener el usuario completo para su nombre completo
        Usuarios usuario = usuariosService.buscarPorEmail(email).orElse(null);

        model.addAttribute("username", email);
        model.addAttribute("rol", rol);
        model.addAttribute("nombreCompleto", usuario != null ? usuario.getNombreCompleto() : email);

        return "home";
    }
}