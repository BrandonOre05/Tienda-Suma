package com.brandonorellana.Tienda_Suma.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuarios implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;

    @NotBlank(message = "El nombre no puede ir vacío.")
    @Size(min = 2, max = 60, message = "El nombre debe tener entre 2 y 60 caracteres.")
    @Column(name = "nombre", nullable = false, length = 60)
    private String nombre;

    @NotBlank(message = "El apellido no puede ir vacío.")
    @Size(min = 2, max = 60, message = "El apellido debe tener entre 2 y 60 caracteres.")
    @Column(name = "apellido", nullable = false, length = 60)
    private String apellido;

    @Pattern(regexp = "^[0-9]{8}$", message = "El teléfono debe contener 8 números.")
    @Column(name = "telefono", length = 15)
    private String telefono;

    @Size(max = 100, message = "La dirección no puede exceder los 100 caracteres.")
    @Column(name = "direccion", length = 100)
    private String direccion;

    @NotBlank(message = "El email no puede estar vacío.")
    @Email(message = "El email debe tener un formato válido.")
    @Column(name = "email", nullable = false, unique = true, length = 60)
    private String email;

    @NotBlank(message = "La contraseña no puede ir vacía.")
    @Size(min = 8, max = 60, message = "La contraseña debe tener entre 8 y 60 caracteres.")
    @Column(name = "password", nullable = false, length = 60)
    private String password;

    @NotNull(message = "El rol no puede ir vacío.")
    @Enumerated(EnumType.STRING)
    @Column(name = "rol", nullable = false, length = 45)
    private Rol rol;

    @Column(name = "fecha_creacion", updatable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El estado no puede ir vacío.")
    @Column(name = "estado", nullable = false)
    private Boolean estado;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + this.rol.name()));
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.estado != null && this.estado;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.estado != null && this.estado;
    }

    public String getNombreCompleto() {
        return this.nombre + " " + this.apellido;
    }
}