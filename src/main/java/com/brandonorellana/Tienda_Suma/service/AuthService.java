package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.dto.LoginDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterAdminDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterClienteDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterVendedorDTO;
import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthService extends UserDetailsService {
    void registrarCliente(RegisterClienteDTO registroDTO);
    void registrarVendedor(RegisterVendedorDTO registroDTO);
    void registrarAdmin(RegisterAdminDTO registroDTO);
    Usuarios login(LoginDTO loginDTO);
}