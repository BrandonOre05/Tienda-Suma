package com.brandonorellana.Tienda_Suma.controller;

import com.brandonorellana.Tienda_Suma.dto.LoginDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterAdminDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterClienteDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterVendedorDTO;
import com.brandonorellana.Tienda_Suma.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @GetMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String loginForm(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            Model model) {
        if (error != null) {
            model.addAttribute("error", "Email o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("success", "Has cerrado sesión exitosamente");
        }
        model.addAttribute("loginDTO", new LoginDTO());
        return "login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registerClienteDTO", new RegisterClienteDTO());
        return "registro";
    }

    @GetMapping("/registro/admin")
    public String registroAdminForm(Model model) {
        model.addAttribute("registerAdminDTO", new RegisterAdminDTO());
        return "registro-admin";
    }

    @GetMapping("/registro/vendedor")
    public String registroVendedorForm(Model model) {
        model.addAttribute("registerVendedorDTO", new RegisterVendedorDTO());
        return "registro-vendedor";
    }

    @PostMapping("/registro")
    public String registrarCliente(@Valid @ModelAttribute("registerClienteDTO") RegisterClienteDTO dto,
                                   BindingResult result,
                                   Model model,
                                   RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro";
        }

        try {
            authService.registrarCliente(dto);
            redirectAttributes.addFlashAttribute("success", "Registro exitoso. Ahora puedes iniciar sesión.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }

    @PostMapping("/registro/admin")
    public String registrarAdmin(@Valid @ModelAttribute("registerAdminDTO") RegisterAdminDTO dto,
                                 BindingResult result,
                                 Model model,
                                 RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro-admin";
        }

        try {
            authService.registrarAdmin(dto);
            redirectAttributes.addFlashAttribute("success", "Administrador registrado exitosamente.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro-admin";
        }
    }

    @PostMapping("/registro/vendedor")
    public String registrarVendedor(@Valid @ModelAttribute("registerVendedorDTO") RegisterVendedorDTO dto,
                                    BindingResult result,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "registro-vendedor";
        }

        try {
            authService.registrarVendedor(dto);
            redirectAttributes.addFlashAttribute("success", "Vendedor registrado exitosamente.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "registro-vendedor";
        }
    }
}