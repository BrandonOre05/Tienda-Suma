package com.brandonorellana.Tienda_Suma.service;

import com.brandonorellana.Tienda_Suma.dto.LoginDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterAdminDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterClienteDTO;
import com.brandonorellana.Tienda_Suma.dto.RegisterVendedorDTO;
import com.brandonorellana.Tienda_Suma.entity.Rol;
import com.brandonorellana.Tienda_Suma.entity.Usuarios;
import com.brandonorellana.Tienda_Suma.repository.UsuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UsuariosService usuarioService;

    @Autowired
    private UsuariosRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private void validarRegistro(String email, String password, String confirmPassword) {
        if (usuarioService.existsByEmail(email)) {
            throw new RuntimeException("El email ya está registrado");
        }
        if (!password.equals(confirmPassword)) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
    }

    @Override
    public void registrarCliente(RegisterClienteDTO dto) {
        validarRegistro(dto.getEmail(), dto.getPassword(), dto.getConfirmPassword());

        Usuarios usuario = new Usuarios();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(Rol.CLIENTE);
        usuario.setEstado(true);

        usuarioService.crear(usuario);
    }

    @Override
    public void registrarVendedor(RegisterVendedorDTO dto) {
        validarRegistro(dto.getEmail(), dto.getPassword(), dto.getConfirmPassword());

        Usuarios usuario = new Usuarios();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(Rol.VENDEDOR);
        usuario.setEstado(true);

        usuarioService.crear(usuario);
    }

    @Override
    public void registrarAdmin(RegisterAdminDTO dto) {
        validarRegistro(dto.getEmail(), dto.getPassword(), dto.getConfirmPassword());

        Usuarios usuario = new Usuarios();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setEmail(dto.getEmail());
        usuario.setTelefono(dto.getTelefono());
        usuario.setDireccion(dto.getDireccion());
        usuario.setPassword(dto.getPassword());
        usuario.setRol(Rol.ADMIN);
        usuario.setEstado(true);

        usuarioService.crear(usuario);
    }

    @Override
    public Usuarios login(LoginDTO loginDTO) {
        Usuarios usuario = usuarioService.buscarPorEmail(loginDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Email no encontrado"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        if (!usuario.getEstado()) {
            throw new RuntimeException("Usuario inactivo");
        }

        return usuario;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email no encontrado: " + email));
    }
}