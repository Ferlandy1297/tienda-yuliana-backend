package com.example.demo.security;

import com.example.demo.user.UsuarioSis;
import com.example.demo.user.UsuarioSisRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private final UsuarioSisRepository usuarioRepo;

    public JpaUserDetailsService(UsuarioSisRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsuarioSis u = usuarioRepo.findByNombreUsuarioAndActivoTrue(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        String role = u.getRol();
        GrantedAuthority auth = new SimpleGrantedAuthority("ROLE_" + role);
        return new User(u.getNombreUsuario(), u.getPasswordHash(), List.of(auth));
    }
}

